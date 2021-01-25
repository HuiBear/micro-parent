package com.micro.ykh.common.controller.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName LoginVO
 * @Description TODO
 * @Author xiongh
 * @Date 2020/11/30 11:38
 * @Version 1.0
 **/

public class LoginVO {

    /**
     * clientId
     */
    @NotBlank(message = "clientId不能为空")
    private String clientId;
    /**
     * 客户端密钥
     */
    @NotBlank(message = "clientSecret不能为空")
    private String clientSecret;
    /**
     * 授权模式
     */
    @NotBlank(message = "grantType不能为空")
    private String grantType;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 平台类型：H5、wx小程序等
     */
    @NotBlank(message = "platformType不能为空")
    private String platformType;
    /**
     * 图形验证码
     */
    private String validationCode;
    /**
     * 获得验证码的key
     */
    private String uuid;
    /**
     * 手机号
     */
    private String mobilePhone;
    /**
     * 登录模式：用户名+密码 or 手机+验证码
     */
    @NotBlank(message = "loginMode不能为空")
    private String loginMode;
    /**
     *手机验证码
     */
    private String smsCode;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", grantType='" + grantType + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", platformType='" + platformType + '\'' +
                ", validationCode='" + validationCode + '\'' +
                ", uuid='" + uuid + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", loginMode='" + loginMode + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }
}
