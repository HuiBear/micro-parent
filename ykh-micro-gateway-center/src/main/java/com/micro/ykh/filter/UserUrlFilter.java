package com.micro.ykh.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.event.PredicateArgsEvent;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * @ClassName UserUrlFilter
 * @Description /fwt-platform/user/....等路径转化成/user/......用户中心路径
 * @Author xiongh
 * @Date 2021/1/20 14:20
 * @Version 1.0
 **/
@Component
public class UserUrlFilter implements GlobalFilter, Ordered {

    private static Logger logger = LogManager.getLogger(UserUrlFilter.class);

    /**
     * 用户中心的路径前缀
     */
    private static final String CONTEXT_PATH = "user";

    private static final String USER_SERVICE_NAME = "user-center";

    private static final String USER_CENTER_ROUTE_ID = "user-center-route";

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RouteLocator routeLocator;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String[] arrs = path.split("/");
        // 如果访问路径中的第二个前缀不是user就直接通过
        if (!StringUtils.equals(arrs[2], CONTEXT_PATH)) {
            return chain.filter(exchange);
        }
        // 将路径的第一个前缀截取掉
        ServerWebExchangeUtils.addOriginalRequestUrl(exchange, exchange.getRequest().getURI());
        String rawPath = exchange.getRequest().getURI().getRawPath();
        String newPath = "/" + Arrays.stream(org.springframework.util.StringUtils.tokenizeToStringArray(rawPath, "/"))
                .skip(1L).collect(Collectors.joining("/"));
        // 修改该路径的路由规则
        Flux<Route> routeFlux = routeLocator.getRoutes();
        ServerHttpRequest newRequest = exchange.getRequest().mutate().path(newPath).build();
        ServerWebExchange newServerWebExchange = exchange.mutate().request(newRequest).build();
        // 将用户中心的路由规则放入到ServerWebExchage中n
        routeFlux.subscribe(r ->{
            if(StringUtils.equals(r.getId(),USER_CENTER_ROUTE_ID)){
                newServerWebExchange.getAttributes().put(GATEWAY_ROUTE_ATTR,r);
            }
        });
        return chain.filter(newServerWebExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String getUrlByServiceName() {
        List<ServiceInstance> urlList = discoveryClient.getInstances(USER_SERVICE_NAME);
        int index = 0;
        if (urlList.size() > 1) {
            index = new Random().nextInt(urlList.size());
        }
        ServiceInstance serviceInstance = urlList.get(index);
        return serviceInstance.getUri().toString();
    }


    public static void main(String[] args) {
        String path = "/fwt-platform/user/login";
        String[] arrs = path.split("/");
        if (StringUtils.equals(arrs[2], CONTEXT_PATH)) {
            path = StringUtils.substring(path, arrs[1].length() + 1);
            System.out.println(path);
        }
    }
}
