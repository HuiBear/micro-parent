package com.micro.ykh.sms.config;


import com.micro.ykh.sms.api.ISMSApi;
import com.micro.ykh.sms.api.impl.TencentSMSApiImpl;
import com.micro.ykh.sms.tencent.TencentSMSInvokeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 获取手机短信验证码的配置
 * @author lzb
 */
@Configuration
public class SmsConfig {

    @Autowired
    private TencentSMSConfig config;

    @Bean
    public ISMSApi ConfigSSMApi() {
        TencentSMSInvokeConfig invokeConfig = new TencentSMSInvokeConfig();
        invokeConfig.setSecretId(config.getSecretId());
        invokeConfig.setSecretKey(config.getSecretKey());

        HashMap<String, String> apps = invokeConfig.getSSMApps();
        config.getApps().forEach(s -> {
            String sign = s.getSign();
            if (apps.containsKey(sign)) {
                return;
            }

            apps.put(sign, s.getSmsSdkAppid());
        });

        HashMap<String, String> txtTemplates = new HashMap<>();
        config.getTxtTemplates().forEach(s -> {
            String code = s.getCode();
            if (txtTemplates.containsKey(code)) {
                return;
            }
            txtTemplates.put(code, s.getId());
        });

        return new TencentSMSApiImpl(invokeConfig, txtTemplates);
    }
}
