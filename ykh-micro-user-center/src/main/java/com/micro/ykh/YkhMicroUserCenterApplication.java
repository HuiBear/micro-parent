package com.micro.ykh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties
@EnableTransactionManagement
@MapperScan(basePackages = {"com.micro.ykh.dao.*"})
public class YkhMicroUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(YkhMicroUserCenterApplication.class, args);
    }

}
