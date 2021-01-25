package com.micro.ykh.common.controller;

import com.micro.ykh.common.service.AuthService;
import com.micro.ykh.constant.FwtConstant;
import com.micro.ykh.common.controller.model.LoginVO;
import com.micro.ykh.domain.AjaxResult;
import com.micro.ykh.type.UserLoginMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.OAuth2AccessToken;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @ClassName AuthController
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/21 16:33
 * @Version 1.0
 **/
@RestController
public class AuthController {


    private static Logger logger = LogManager.getLogger(AuthController.class);


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public AjaxResult login(@Validated @RequestBody LoginVO loginVO) {
        // 手机验证码和图形验证码验证
        if (!checkCode(loginVO).get("code").equals(HttpStatus.OK.value())) {
            return checkCode(loginVO);
        }
        OAuth2AccessToken token = authService.grantToken(loginVO);
        return AjaxResult.success(token);
    }

    /**
     * 验证码check
     *
     * @param loginVO vo
     * @return R
     */
    private AjaxResult checkCode(LoginVO loginVO) {
        // 如果是测试帐号，直接通过，不去验证
        if (StringUtils.isNotBlank(loginVO.getMobilePhone())
                && (FwtConstant.SHOP_APP_TEST_ACCOUNT.equals(loginVO.getMobilePhone())
                || FwtConstant.CLIENT_APP_TEST_ACCOUNT.equals(loginVO.getMobilePhone()))) {
            return AjaxResult.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        }
        // 先判断登录方式，来验证图形验证码或者手机验证码
        if (UserLoginMode.PASSWORD.getLoginMode().equals(loginVO.getLoginMode())) {
            if (StringUtils.isBlank(loginVO.getUuid())) {
                logger.error("{},uuid不能为空", loginVO.getUsername());
                return AjaxResult.error(20009, "uuid不能为空");
            }
            String verifyCode = FwtConstant.CAPTCHA_CODE_KEY + loginVO.getUuid();
            String validationCode = stringRedisTemplate.opsForValue().get(verifyCode);
            stringRedisTemplate.delete(verifyCode);
            if (StringUtils.isBlank(validationCode)) {
                logger.warn("{},验证码已失效，重新获得", loginVO.getUsername());
                return AjaxResult.error(20007, "验证码已失效，重新获得");
            }
            if (!validationCode.equals(loginVO.getValidationCode())) {
                logger.warn("{},验证码不正确，请重新获得！", loginVO.getUsername());
                return AjaxResult.error(20008, "验证码不正确，请重新获得");
            }
        }
        // 是手机登录模式
        if (UserLoginMode.MOBILE_PHONE.getLoginMode().equals(loginVO.getLoginMode())) {
            // 先从redis获得手机验证码
            loginVO.setUsername(loginVO.getMobilePhone());
            if (StringUtils.isBlank(loginVO.getMobilePhone())) {
                logger.warn("手机号码不能为空");
                return AjaxResult.error(20009, "手机号码不能为空");
            }
            String smsRedisKey = FwtConstant.SMS_CODE_KEY + loginVO.getPlatformType() + "_" + loginVO.getMobilePhone();
            String smsCode = stringRedisTemplate.opsForValue().get(smsRedisKey);
            if (StringUtils.isBlank(smsCode)) {
                logger.warn("{}，手机验证码已过期", loginVO.getMobilePhone());
                return AjaxResult.error(20005, "手机验证码已过期");
            }
            if (!smsCode.equals(loginVO.getSmsCode())) {
                logger.warn("{},手机验证码不正确", loginVO.getMobilePhone());
                return AjaxResult.error(20006, "手机验证码不正确");
            }
            stringRedisTemplate.delete(smsRedisKey);
        }
        return AjaxResult.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }


    @PostMapping("/exit")
    public AjaxResult logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request);
        return AjaxResult.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    @PostMapping(value = "/refresh_token")
    public AjaxResult refreshToken(@Validated @RequestBody RefreshTokenVO vo) {
        vo.checkUserAndPhoneByLoginMode();
        return AjaxResult.success(authService.grantTokeByRefreshToken(vo));
    }

}
