package com.micro.ykh.sms.api;

import org.apache.commons.lang3.tuple.Triple;

public interface ISMSApi {
    /**
     * @Title: sendCode
     * @Description:
     * @param code: 发送的验证码
     * @param timeoutMinute: 验证码超时时间
     * @param phoneNumbers:  发送的电话号码，采用 e.164 标准，格式为+[国家或地区码][手机号]，单次请求最多支持200个手机号且要求全为境内手机号或全为境外手机号
     * @return: org.apache.commons.lang3.tuple.Triple<java.lang.String, java.lang.Boolean, java.lang.String>[] 返回信息为针对单个手机发送结果<电话号码，结果，结果描述></>。
     * @Author: zyj
     * @Date: 2020/12/7
     */
    Triple<String, Boolean, String>[] sendVerificationCode(String sign, String code, int timeoutMinute, String... phoneNumbers);
}
