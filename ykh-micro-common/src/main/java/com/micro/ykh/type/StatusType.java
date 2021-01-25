package com.micro.ykh.type;

public enum StatusType {

    DISABLE(0,"停用"),

    ENABLE(1,"启用");

    private Integer value;
    private String description;

    StatusType(Integer value, String description) {
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
