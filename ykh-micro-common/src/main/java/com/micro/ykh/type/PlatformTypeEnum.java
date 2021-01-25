package com.micro.ykh.type;

public enum PlatformTypeEnum {

    /**
     * 平台端
     */
    PLATFORM("platform"),

    /**
     * 物业端
     */
    PROPERTY_SERVICE("property_service"),

    /**
     * 客户端app
     */
    CLIENT_APP("client_app"),

    /**
     * 店铺端app
     */
    SHOP_APP("shop_app")
    ;


    private String value;

    PlatformTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
