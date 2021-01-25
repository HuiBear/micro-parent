package com.micro.ykh.error;

public enum UserCenterErrorType implements ErrorType{

    /**
     * 店铺端，没有此用户
     */
    NO_QUERY_SHOP_STAFF(100001,"店铺端没有此用户！"),

    /**
     * 客户端，没有些用户
     */
    NO_QUERY_CLIENT_USER(100002,"客户端没有此用户！")
    ;

    private Integer code;
    private String message;

    UserCenterErrorType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
