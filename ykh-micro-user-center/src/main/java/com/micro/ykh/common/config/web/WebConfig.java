package com.micro.ykh.common.config.web;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName WebConfig
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/21 17:25
 * @Version 1.0
 **/
@Configuration
public class WebConfig {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }




}
