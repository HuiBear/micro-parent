package com.micro.ykh.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.ykh.common.controller.RefreshTokenVO;
import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.domain.AjaxResult;
import com.micro.ykh.exception.CustomException;
import com.micro.ykh.type.UserLoginMode;
import com.micro.ykh.utils.sign.Des64Utils;
import com.micro.ykh.utils.sign.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AuthService
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/21 9:04
 * @Version 1.0
 **/
@Service
public class AuthService {

    private static Logger logger = LogManager.getLogger(AuthService.class);

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 授权，获得token
     *
     * @param loginVO
     * @return OAuth2AccessToken
     */
    public OAuth2AccessToken grantToken(LoginVO loginVO) {
        // 获取授权码
        OAuth2AccessToken token = getAccessToken(loginVO);

        // 将用户信息放入到redis中
        Map<String, String> additionInfoMap = (Map<String, String>) token.getAdditionalInformation().get("additionalInfo");
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", additionInfoMap.get("userId"));
        userMap.put("platformType", loginVO.getPlatformType());
        userMap.put("loginMode", loginVO.getLoginMode());
        userMap.put("nickName", additionInfoMap.get("nickName"));
        if (UserLoginMode.MOBILE_PHONE.getLoginMode().equals(loginVO.getLoginMode())) {
            userMap.put("phone", loginVO.getMobilePhone());
            userMap.put("userName", "");
        } else {
            userMap.put("userName", loginVO.getUsername());
            userMap.put("phone", "");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String userInfo = null;
        try {
            userInfo = objectMapper.writeValueAsString(userMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CustomException("用户信息json转换错误！");
        }
        stringRedisTemplate.opsForValue().set(token.getValue(), userInfo, token.getExpiresIn(), TimeUnit.SECONDS);
        return token;
    }

    /**
     * @return org.springframework.security.oauth2.common.OAuth2AccessToken
     * @Author xiongh
     * @Description 通过refreshToken重新获得accessToken
     * @Date 2021/1/21 11:07
     * @Param [vo]
     **/
    public OAuth2AccessToken grantTokeByRefreshToken(RefreshTokenVO vo) {
        // 解密clientId和clientSecret
        String clientId = Des64Utils.unDes64(vo.getClientId(), FwtConstant.DES_PASSWORD_KEY, FwtConstant.DES_PASSWORD_VI);
        String clientSecret = Des64Utils.unDes64(vo.getClientSecret(), FwtConstant.DES_PASSWORD_KEY, FwtConstant.DES_PASSWORD_VI);
        ClientDetails clientDetails = checkClientDetails(clientId, clientSecret);
        // 获得refreshToken
        String refreshKey = generateKey(vo.getLoginMode(), vo.getPlatformType(), vo.getPhone(), vo.getUserName());
        String refreshToken = stringRedisTemplate.opsForValue().get(refreshKey);
        if (StringUtils.isBlank(refreshToken)) {
            logger.warn("refreshToken已过期！");
            throw new CustomException(10060, "refreshToken已过期请重新登录！");
        }

        Map<String, String> multiValueMap = new LinkedHashMap<>();
        multiValueMap.put("grant_type", "refresh_token");
        multiValueMap.put("refresh_token", refreshToken);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(clientId, null, null);

        OAuth2AccessToken token = null;
        try {
            token = tokenEndpoint.postAccessToken(usernamePasswordAuthenticationToken, multiValueMap).getBody();
            return token;
        } catch (HttpRequestMethodNotSupportedException e) {
            e.printStackTrace();
            throw new CustomException("获取token失败，请重新登录！");
        }

    }

    /**
     * @return void
     * @Author xiongh
     * @Description // 登出
     * @Date 2021/1/21 11:43
     * @Param [request]
     **/
    public void logout(HttpServletRequest request) {
        // 先要将redis中此用户的refreshToken清除
        String token = StringUtils.substringAfter(request.getHeader("Authorization"), "bearer ");
        String userInfo = stringRedisTemplate.opsForValue().get(token);
        try {
            Map<String, String> map = new ObjectMapper().readValue(userInfo, Map.class);
            String refreshKey = generateKey(map.get("loginMode"), map.get("platformType"), map.get("phone"), map.get("userName"));
            stringRedisTemplate.delete(refreshKey);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CustomException("用户信息json转换错误！");
        }
        // 除此token下的用户redis缓存
        stringRedisTemplate.delete(token);
        // 将此token放入redis的黑名单,此key的过期时间是token的剩余过期时间
        String tokenKey = jwtAccessTokenConverter.getKey().get("value");
        PublicKey publicKey = null;
        try {
            publicKey = JwtUtils.getPublicKey(tokenKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new CustomException("tokenKey无效！");
        }
        Date expirationDate = JwtUtils.getExpirationDate(token, publicKey);
        long expirationTime = expirationDate.getTime() - System.currentTimeMillis();
        if (expirationTime > 0) {
            stringRedisTemplate.opsForValue().set(FwtConstant.TOKEN_BLACK_LIST_KEY + token, "1", expirationTime, TimeUnit.MILLISECONDS);
        }
        new SecurityContextLogoutHandler().logout(request, null, null);
    }

    /**
     * 获得token
     *
     * @param loginVO
     * @return
     */
    private OAuth2AccessToken getAccessToken(LoginVO loginVO) {
        // 解密clientId
        String clientId = Des64Utils.unDes64(loginVO.getClientId(), FwtConstant.DES_PASSWORD_KEY, FwtConstant.DES_PASSWORD_VI);
        String clientSecret = Des64Utils.unDes64(loginVO.getClientSecret(), FwtConstant.DES_PASSWORD_KEY, FwtConstant.DES_PASSWORD_VI);
        // 先判断clientId和clientSecret在表中是否能够匹配
        ClientDetails clientDetails = checkClientDetails(clientId, clientSecret);

        Map<String, String> multiValueMap = new LinkedHashMap<>();
        multiValueMap.put("grant_type", loginVO.getGrantType());
        multiValueMap.put("username", loginVO.getUsername());
        multiValueMap.put("platformType", loginVO.getPlatformType());
        multiValueMap.put("loginMode", loginVO.getLoginMode());
        multiValueMap.put("mobilePhone", loginVO.getMobilePhone());
        // 手机登录的话用手机做为密码
        if(UserLoginMode.MOBILE_PHONE.getLoginMode().equals(loginVO.getLoginMode())){
            multiValueMap.put("password", loginVO.getMobilePhone());
        }else{
            multiValueMap.put("password", loginVO.getPassword());
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(clientId, null, null);
        try {
            OAuth2AccessToken token = tokenEndpoint.postAccessToken(usernamePasswordAuthenticationToken, multiValueMap).getBody();
            // 将refreshToken放到redis中
            if (token == null) {
                logger.warn("OAuth2AccessToken为空！");
                throw new CustomException(10070, "没有获得到token，请重新登录！");
            }
            // 将用户的refreshToken存入到redis中，当token过期，可以用refreshToken重新获得token，不用登录用户名和密码
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            // 密码登录用platformType+用户名来做key  手机登录用platformType+手机号
            String refreshKey = generateKey(loginVO.getLoginMode(), loginVO.getPlatformType(), loginVO.getMobilePhone(), loginVO.getUsername());
            stringRedisTemplate.opsForValue().set(refreshKey, refreshToken.getValue(), clientDetails.getRefreshTokenValiditySeconds(), TimeUnit.SECONDS);
            return token;
        } catch (HttpRequestMethodNotSupportedException e) {
            logger.error(e.getMessage());
            throw new CustomException("获取token失败！");
        }
    }

    private ClientDetails checkClientDetails(String clientId, String clientSecret) {
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (ObjectUtils.isEmpty(clientDetails)) {
            throw new CustomException("此clientId不存在！");
        }
        // 判断clientSecret是否匹配
        if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new CustomException("clientSecret不正确！");
        }
        return clientDetails;
    }

    private String generateKey(String loginMode, String platformType, String phone, String userName) {
        String refreshKey;
        if (StringUtils.equals(UserLoginMode.MOBILE_PHONE.getLoginMode(), loginMode)) {
            refreshKey = FwtConstant.REFRESH_TOKEN_KEY + platformType + "_" + phone;
        } else {
            refreshKey = FwtConstant.REFRESH_TOKEN_KEY + platformType + "_" + userName;
        }
        return refreshKey;
    }

}
