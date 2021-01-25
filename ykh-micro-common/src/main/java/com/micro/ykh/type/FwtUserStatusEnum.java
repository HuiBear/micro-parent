package com.micro.ykh.type;

/**
 * 福物通用户状态
 */
public enum FwtUserStatusEnum {

    /**
     * 未锁
     */
    UN_LOCKED(1),

    /**
     * 已锁
     */
    LOCKED(0);

    private Integer status;

    FwtUserStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
