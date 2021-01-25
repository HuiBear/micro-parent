package com.micro.ykh.sms.tencent;





import java.util.HashMap;


public class TencentSMSInvokeConfig {
    public TencentSMSInvokeConfig() {
        endpoint = "sms.tencentcloudapi.com";
    }

    private String secretId;
    private String secretKey;
    private String endpoint;


    private final HashMap<String, String> SSMApps = new HashMap<>();

    public HashMap<String, String> getSSMApps() {
        return SSMApps;
    }

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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}

