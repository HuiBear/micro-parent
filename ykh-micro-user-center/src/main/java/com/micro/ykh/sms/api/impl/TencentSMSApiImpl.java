package com.micro.ykh.sms.api.impl;


import com.micro.ykh.sms.UnSMSSupportException;
import com.micro.ykh.sms.api.ISMSApi;
import com.micro.ykh.sms.tencent.TencenSMS;
import com.micro.ykh.sms.tencent.TencentSMSInvokeConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Title:
 * @Description: 用于腾讯云的短信发送
 * @return:
 * @Author: zyj
 * @Date: 2020/12/7
 */
public class TencentSMSApiImpl implements ISMSApi {
    private TencenSMS tencenSMS;
    private final HashMap<String, String> txtTemplateIds = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TencentSMSApiImpl.class);

    public TencentSMSApiImpl(TencentSMSInvokeConfig config, HashMap<String, String> txtTemplateIds) {
        if (config == null) {
            throw new IllegalArgumentException("配置信息不能为空");
        }
        this.tencenSMS = new TencenSMS(config);
        this.txtTemplateIds.putAll(txtTemplateIds);
    }

    @Override
    public Triple<String, Boolean, String>[] sendVerificationCode(String sign, String code, int timeoutMinute, String... phoneNumbers) {
        String txtTemplateId = "";
        String key = "sendVerificationCode";
        if (this.txtTemplateIds.containsKey(key)) {
            txtTemplateId = this.txtTemplateIds.get(key);
        }

        if (StringUtils.isBlank(txtTemplateId)) {
            throw new UnSMSSupportException(String.format("发送验证码模板未配置%s", txtTemplateId));
        }

        return this.tencenSMS.sendSms(sign, phoneNumbers, txtTemplateId, new String[]{code, String.valueOf(timeoutMinute)});
    }
}