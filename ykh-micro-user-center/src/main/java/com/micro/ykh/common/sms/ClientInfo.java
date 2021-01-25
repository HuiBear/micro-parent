package com.micro.ykh.common.sms;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 请求接口的基本设备及版本信息
 */
public class ClientInfo {
    /**
     * 调用平台：IOS/ANDORID/WEB/WXSMALL
     */
    private String platform;
    /**
     *具体调用的应用名称，如：fuwutong（福物通）
     */
    @NotBlank(message = "请求头部中应用名称不能为空")
    private String applicationName;
    /**
     * 版本：在APP端的应用版本号
     */
    private String version;
    /**
     * H5版本：内置在APP端的H5版本
     */
    private String h5Version;
    /**
     * 系统版本号
     */
    private String systemVersion;
    /**
     * 设备型号
     */
    private String device;

    private String phone;

    @NotNull(message = "平台类别不能为空")
    private String platformType;

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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getH5Version() {
        return h5Version;
    }

    public void setH5Version(String h5Version) {
        this.h5Version = h5Version;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "platform='" + platform + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", version='" + version + '\'' +
                ", h5Version='" + h5Version + '\'' +
                ", systemVersion='" + systemVersion + '\'' +
                ", device='" + device + '\'' +
                '}';
    }
}
