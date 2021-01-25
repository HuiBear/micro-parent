package com.micro.ykh.sms.config;


import com.micro.ykh.sms.tencent.TencentSMSApp;
import com.micro.ykh.sms.tencent.TencentSMSTxtTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 获取手机短信验证码的配置
 * @author lzb
 */
@Component
@ConfigurationProperties(prefix = "tencentsms")
public class TencentSMSConfig {

    private String secretId;

    private String secretKey;

    private ArrayList<TencentSMSApp> apps;

    private ArrayList<TencentSMSTxtTemplate> txtTemplates;

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public ArrayList<TencentSMSApp> getApps() {
        return apps;
    }

    public void setApps(ArrayList<TencentSMSApp> apps) {
        this.apps = apps;
    }

    public ArrayList<TencentSMSTxtTemplate> getTxtTemplates() {
        return txtTemplates;
    }

    public void setTxtTemplates(ArrayList<TencentSMSTxtTemplate> txtTemplates) {
        this.txtTemplates = txtTemplates;
    }
}
