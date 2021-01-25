package com.micro.ykh.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.micro.ykh.component.common.MDA;
import com.micro.ykh.common.config.ShouldSkipUrlsProperties;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.exception.GateWayException;
import com.micro.ykh.exception.SystemErrorType;
import com.micro.ykh.common.handle.UrlPermissionContext;
import com.micro.ykh.common.handle.UrlPermissionInterface;
import com.micro.ykh.utils.sign.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.*;

/**
 * @ClassName AuthorizationFilter
 * @Description 认证过滤器, 根据url判断用户请求是要经过认证 才能访问
 * @Author xiongh
 * @Date 2020/11/19 16:24
 * @Version 1.0
 **/
@Component
public class AuthorizationFilter implements GlobalFilter, Ordered, CommandLineRunner {

    private static Logger logger = LogManager.getLogger(AuthorizationFilter.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    private PublicKey publicKey;

    @Autowired
    private ShouldSkipUrlsProperties shouldSkipUrlsProperties;

    @Autowired
    private UrlPermissionContext urlPermissionContext;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获得请求的url,并且判断它是否是shouldSkipUrl
        String reqUrl = exchange.getRequest().getURI().getPath();
        logger.info("请求地址：{}", reqUrl);
        if (shouldSkipUrl(reqUrl)) {
            return chain.filter(exchange);
        }
        // 1.先获得请求头
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        /**
         * 2.判断请求头是否为空
         *   1).如果为空 -> 抛出异常
         *   2).如果不空 -> 验证是否有效
         */
        if (StringUtils.isEmpty(authHeader)) {
            logger.warn("需要认证的url,请求头为空");
            throw new GateWayException(SystemErrorType.UNAUTHORIZED_HEADER_IS_EMPTY);
        }
        String token = StringUtils.substringAfter(authHeader, "bearer ");

        Claims claims = validateJwtToken(token);

        // 登出路径不判断后面权限
        if (reqUrl.equals(shouldSkipUrlsProperties.getLogoutUrl())) {
            return chain.filter(exchange);
        }

        // 判断该客户端是否有权限访问这个资源服务器
        if (!checkResourcePermission(claims, reqUrl)) {
            logger.warn("此token没有权限访问这个资源服务器");
            throw new GateWayException(SystemErrorType.FORBIDDEN);
        }
        // 判断是否有权限
        try {
            hasPermission(claims, reqUrl, token);
        } catch (JsonProcessingException e) {
            throw new GateWayException(SystemErrorType.JSON_TRANSFER_ERROR);
        }
        return chain.filter(exchange);
    }


    /**
     * 从认证服务获得公钥
     *
     * @return
     */
    private PublicKey getPublicKeyByTokenKey() {
        try {
            String tokenKey = getTokenKey();

            if (StringUtils.isEmpty(tokenKey)) {
                throw new GateWayException(SystemErrorType.GET_TOKEN_KEY_ERROR);
            }

            PublicKey publicKey = JwtUtils.getPublicKey(tokenKey);

            logger.info("生成公钥:{}", publicKey);

            return publicKey;
        } catch (Exception e) {
            logger.info("生成公钥异常:{}", e.getMessage());
            throw new GateWayException(SystemErrorType.GET_TOKEN_KEY_ERROR);
        }
    }

    /**
     * 方法实现说明:去认证服务器上获取tokenKey
     *
     * @return
     */
    private String getTokenKey() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(MDA.clientId, MDA.clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);

        try {

            ResponseEntity<Map> response = restTemplate.exchange(MDA.getTokenKey, HttpMethod.GET, entity, Map.class);

            String tokenKey = response.getBody().get("value").toString();

            logger.info("去认证服务器获取TokenKey:{}", tokenKey);

            return tokenKey;
        } catch (Exception e) {
            logger.error("远程调用认证服务器获取tokenKey失败:{}", e.getMessage());
            throw new GateWayException(SystemErrorType.GET_TOKEN_KEY_ERROR);
        }
    }

    /**
     * 判断请求路径是否在需要跳过的路径里
     *
     * @param url
     * @return
     */
    private boolean shouldSkipUrl(String url) {
        String[] skipUrls = shouldSkipUrlsProperties.getShouldSkipUrls();
        for (String skipUrl : skipUrls) {
            if (url.contains(skipUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证jwtToken
     *
     * @param token 请求头信息
     * @return
     */
    private Claims validateJwtToken(String token) {
        try {
            /*
             * 由于jwt的token只能过期才会清除，当用户登出的时候会将此token放到redis中,做为黑名单；
             * 所以在网关中获得token后要去redis验证此token是否已经登出
             *
             */
            if (!ObjectUtils.isEmpty(redisTemplate.opsForValue().get(FwtConstant.TOKEN_BLACK_LIST_KEY + token))) {
                logger.warn("此token已无效！");
                throw new GateWayException(SystemErrorType.INVALID_TOKEN);
            }
            Jwt<JwsHeader, Claims> parseClaimsJwt = JwtUtils.getJwtByTokenKey(token, publicKey);
            Claims claims = parseClaimsJwt.getBody();
            //判断token是否过期
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                logger.warn("token已过期！");
                throw new GateWayException(SystemErrorType.TOKEN_TIMEOUT);
            }
            logger.info("claims:{}", claims);
            return claims;
        } catch (Exception e) {
            logger.error("校验token异常:{},异常信息:{}", token, e.getMessage());
            throw new GateWayException(SystemErrorType.INVALID_TOKEN);
        }
    }

    private void hasPermission(Claims claims, String currentUrl, String token) throws JsonProcessingException {
        UrlPermissionInterface urlPermissionInterface = urlPermissionContext.init(token);
        boolean hasPermission = urlPermissionInterface.checkUrlPermission(claims, currentUrl);
        if (!hasPermission) {
            logger.warn("没有访问该接口的权限！");
            throw new GateWayException(SystemErrorType.FORBIDDEN);
        }
    }


    /**
     * 判断此token是否有权限访问这个资源服务器
     *
     * @return boolean
     */
    private boolean checkResourcePermission(Claims claims, String currentUrl) {
        boolean hasResourcePermission = false;
        // 路径前缀名就是资源服务ID
        String[] resourceIds = currentUrl.split("/");
        List<String> resources = claims.get("aud", List.class);
        for (String resource : resources) {
            if (resource.equals(resourceIds[1])) {
                hasResourcePermission = true;
                break;
            }
        }
        return hasResourcePermission;

    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void run(String... args) throws Exception {
        // 通过tokenKey获得公钥
        this.publicKey = getPublicKeyByTokenKey();
    }
}
