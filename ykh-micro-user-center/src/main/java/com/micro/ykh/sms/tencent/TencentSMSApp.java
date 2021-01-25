package com.micro.ykh.sms.tencent;



/**
 * 对应短信应用
 * @Title: a
 * @Description:
 * @return:
 * @Author: zyj
 * @Date: 2020/12/9
 */

public class TencentSMSApp {
    private String smsSdkAppid;
    private String sign;

    public String getSmsSdkAppid() {
        return smsSdkAppid;
    }

    public void setSmsSdkAppid(String smsSdkAppid) {
        this.smsSdkAppid = smsSdkAppid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "TencentSMSApp{" +
                "smsSdkAppid='" + smsSdkAppid + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}

