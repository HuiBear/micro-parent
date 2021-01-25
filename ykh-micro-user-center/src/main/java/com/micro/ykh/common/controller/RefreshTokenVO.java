package com.micro.ykh.common.controller;

import com.micro.ykh.exception.CustomException;
import com.micro.ykh.type.UserLoginMode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName RefreshTokenVO
 * @Description 刷新token获得授权码
 * @Author xiongh
 * @Date 2021/1/21 10:41
 * @Version 1.0
 **/
public class RefreshTokenVO {

    @NotBlank(message = "clientId不能为空")
    private String clientId;
    @NotBlank(message = "clientSecret不能为空")
    private String clientSecret;
    private String userName;
    private String phone;
    @NotBlank(message = "platformType不能为空")
    private String platformType;
    @NotBlank(message = "loginMode不能为空")
    private String loginMode;

    public String getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "RefreshTokenVO{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", userName='" + userName + '\'' +
                ", platformType='" + platformType + '\'' +
                '}';
    }

    void checkUserAndPhoneByLoginMode() {
        if (StringUtils.equals(UserLoginMode.MOBILE_PHONE.getLoginMode(), this.loginMode)) {
            if (StringUtils.isNotBlank(this.phone)) {
                throw new CustomException("phone不能为空！");
            }
        } else {
            if (StringUtils.isNotBlank(this.userName)) {
                throw new CustomException("用户名不能为空！");
            }
        }
    }
}
