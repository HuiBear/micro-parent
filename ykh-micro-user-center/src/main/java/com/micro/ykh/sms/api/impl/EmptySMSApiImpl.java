package com.micro.ykh.sms.api.impl;

import com.micro.ykh.sms.api.ISMSApi;
import org.apache.commons.lang3.tuple.Triple;

/**
 * @Title:
 * @Description: 提供empty模式，默认不发送验证码
 * @return:
 * @Author: zyj
 * @Date: 2020/12/7
 */
public class EmptySMSApiImpl implements ISMSApi {
    @Override
    public Triple<String, Boolean, String>[] sendVerificationCode(String sign, String code, int timeoutMinute, String... phoneNumbers) {
        return new Triple[0];
    }
}
