package com.micro.ykh.sms.tencent;

import com.micro.ykh.sms.UnSMSSupportException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Title: Tencen SSM
 * @Description: 腾讯云发送短信的工具类
 * @return:
 * @Author: zyj
 * @Date: 2020/12/8
 */
public class TencenSMS {
    private TencentSMSInvokeConfig config;
    private Credential credential;
    private static final Logger logger = LoggerFactory.getLogger(TencenSMS.class);

    public TencenSMS(TencentSMSInvokeConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("配置信息不能为空");
        }

        this.config = config;
        this.credential = new Credential(this.config.getSecretId(), this.config.getSecretKey());
    }

    /**
     * @param appName:对应app的签名内容
     * @param phoneNumbers:
     * @param txtTemplateId:
     * @param templateParamSet:
     * @Title: sendSms
     * @Description: 给指定的手机号，发送指定的短信模板
     * @return: org.apache.commons.lang3.tuple.Triple<java.lang.String, java.lang.String, java.lang.String>[]
     * @Author: zyj
     * @Date: 2020/12/8
     */
    public Triple<String, Boolean, String>[] sendSms(String appName, String[] phoneNumbers, String txtTemplateId, String... templateParamSet) {
        ArrayList<Triple<String, Boolean, String>> list = new ArrayList<>();
        Triple<String, Boolean, String>[] triples = new Triple[phoneNumbers.length];
        if (phoneNumbers.length == 0) {
            return list.toArray(triples);
        }

        if (!this.config.getSSMApps().containsKey(appName)) {
            throw new UnSMSSupportException(String.format("不支持%s发送短信", appName));
        }

        String appid = this.config.getSSMApps().get(appName);
        try {
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(this.config.getEndpoint());

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(this.credential, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            req.setPhoneNumberSet(phoneNumbers);

            req.setTemplateParamSet(templateParamSet);

            req.setSmsSdkAppid(appid);
            req.setTemplateID(txtTemplateId);
            req.setSign(appName);

            SendSmsResponse resp = client.SendSms(req);
            Arrays.stream(resp.getSendStatusSet())
                    .forEach(s -> {
                        // logger.info(AbstractModel.toJsonString(s));
                        // {"SerialNo":"2019:-4306508021189168639","PhoneNumber":"+8618970808531","Fee":1,"SessionContext":"","Code":"Ok","Message":"send success","IsoCode":"CN"}
                        list.add(Triple.of(s.getPhoneNumber(), "Ok".equals(s.getCode()), s.getMessage()));
                    });

        } catch (Exception e) {
            logger.error("发送短信验证码出错", e);
        }

        return list.toArray(triples);
    }
}