package com.micro.ykh.error;

public interface ErrorType {
    /**
     * 返回code
     *
     * @return
     */
    Integer getCode();

    /**
     * 返回message
     *
     * @return
     */
    String getMessage();
}
