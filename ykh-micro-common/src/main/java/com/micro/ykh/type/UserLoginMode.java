package com.micro.ykh.type;

/**
 * 用户登录模式
 */
public enum UserLoginMode {

    /**
     * 用户+密码模式
     */
    PASSWORD("passwd"),

    /**
     * 手机+手机验证码
     */
    MOBILE_PHONE("phone");

    private String loginMode;

    UserLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    public String getLoginMode() {
        return loginMode;
    }
}
