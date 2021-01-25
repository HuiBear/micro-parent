package com.micro.ykh.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName LoadBalanceConfig
 * @Description TODO
 * @Author xiongh
 * @Date 2020/11/18 22:33
 * @Version 1.0
 **/
@Configuration
public class LoadBalanceConfig {

//    @Bean
//    public SelfRestTemplate selfRestTemplate() {
//        return new SelfRestTemplate();
//    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public ShouldSkipUrlsProperties gatewayProperties() {
        return new ShouldSkipUrlsProperties();
    }
}
