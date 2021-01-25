package com.micro.ykh.sms;



import com.micro.ykh.sms.api.ISMSApi;
import com.micro.ykh.sms.api.impl.TencentSMSApiImpl;
import com.micro.ykh.sms.tencent.TencentSMSInvokeConfig;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        TencentSMSInvokeConfig config = new TencentSMSInvokeConfig();
        config.setSecretId("AKIDd1MRGp5H6sNq2xD5JI2f16leMo9gARyP");
        config.setSecretKey("iDXkgRxcbD8amjRvHtj5A33uUzVYKzdv");
        config.getSSMApps().put("房乐美", "1400458675");

        ISMSApi ssmapi = new TencentSMSApiImpl(config, new HashMap<String, String>() {
            {
                put("sendVerificationCode", "385796");
            }
        });
        // {"SerialNo":"2019:-4306508021189168639","PhoneNumber":"+8618970808531","Fee":1,"SessionContext":"","Code":"Ok","Message":"send success","IsoCode":"CN"}
        ssmapi.sendVerificationCode("房乐美", "222222", 2, "8615979194719");
    }
}
