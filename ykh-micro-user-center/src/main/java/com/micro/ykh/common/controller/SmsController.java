package com.micro.ykh.common.controller;


import com.micro.ykh.common.sms.ClientInfo;
import com.micro.ykh.common.sms.PublicMethod;
import com.micro.ykh.common.sms.R;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.sms.api.ISMSApi;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 获取手机验证码
 *
 * @author lzb
 * @since 2020-12-09
 */
@RestController

public class SmsController {

    @Autowired
    private ISMSApi ismsApi;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final Logger logger = LoggerFactory.getLogger(SmsController.class);


    @PostMapping("/verification_code")
    public R getSmsVerificationCode(@Valid @RequestBody ClientInfo clientInfo) {
        if (clientInfo == null || "".equals(clientInfo.getApplicationName().trim())) {
            return R.error(1005, "应用名称不能为空！");
        }
        if (clientInfo.getPhone() == null || "".endsWith(clientInfo.getPhone().trim())) {
            return R.error(1005, "手机号不能为空！");
        }
        String sign = clientInfo.getApplicationName();
        if (!sign.equals(FwtConstant.APPLICATION_NAME)) {
            return R.error(1007, "无应用名称的应用！");
        }
        logger.info("SmsController Request:[phone=" + clientInfo.getPhone() + "," + clientInfo.getApplicationName() + "]");
        try {
            if (!PublicMethod.isMobileNO(clientInfo.getPhone())) {
                return R.error(1001, "请输入有效的手机号码！");
            }
            String smsRedisKey = FwtConstant.SMS_CODE_KEY + clientInfo.getPlatformType() + "_" + clientInfo.getPhone();
            int timeoutMinute = 5;
            String code = PublicMethod.getCode(6);
            sign = FwtConstant.APP_NAME;
            // 配置腾讯SMS的参数
            Triple<String, Boolean, String>[] result = ismsApi.sendVerificationCode(sign, code, timeoutMinute, FwtConstant.PHONE_HEADER + clientInfo.getPhone());
            // 测试用
//            ArrayList<Triple<String, Boolean, String>> list = new ArrayList<>();
//            Triple<String, Boolean, String>[] result = new Triple[1];
//            list.add(Triple.of("1", true, "123"));
//            result = list.toArray(result);
            if (result.length == 1) {
                if (result[0].getMiddle()) {
                    logger.info("SMS Send:[" + clientInfo.getPhone() + ":" + code + " 短信验证码发送成功！]");
                    stringRedisTemplate.opsForValue().set(smsRedisKey, code, timeoutMinute * 60, TimeUnit.SECONDS);
                    return R.ok(200, "发送成功");
                }
                logger.info("SMS Send:[" + clientInfo.getPhone() + ":" + code + " 短信验证码发送失败！]");
            }
            return R.error(1002, "短信验证码发送失败！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error(500, "服务器内部错误！");
        }
    }
}