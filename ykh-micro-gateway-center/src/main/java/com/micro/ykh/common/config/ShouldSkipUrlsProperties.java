package com.micro.ykh.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName ApplicationProperties
 * @Description TODO
 * @Author xiongh
 * @Date 2020/11/20 10:00
 * @Version 1.0
 **/
@PropertySource("classpath:ykh.properties")
@Component
public class ShouldSkipUrlsProperties {

    @Value("#{'${should.skip.urls}'.split(',')}")
    private String[] shouldSkipUrls;
    @Value("${logout.url}")
    private String logoutUrl;

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String[] getShouldSkipUrls() {
        return shouldSkipUrls;
    }

    public void setShouldSkipUrls(String[] shouldSkipUrls) {
        this.shouldSkipUrls = shouldSkipUrls;
    }
}
