package com.micro.ykh.type;

public enum DelFlagType {

    DELETED(-1,"删除"),

    UN_DELETED(0,"有效");

    private Integer value;
    private String description;

    DelFlagType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
