package com.micro.ykh.exception;



/**
 * 网关异常
 * Created by smlz on 2019/12/26.
 */

public class GateWayException extends RuntimeException {

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GateWayException(SystemErrorType systemErrorType) {
        this.code = systemErrorType.getCode();
        this.msg = systemErrorType.getMessage();
    }

}
