package com.micro.ykh.exception;


import com.micro.ykh.error.ErrorType;

/**
 * 自定义异常
 * 
 * @author ruoyi
 */
public class CustomException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    public CustomException(String message)
    {
        this.message = message;
    }

    public CustomException(Integer code,String message)
    {
        this.message = message;
        this.code = code;
    }

    public CustomException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

    public CustomException(ErrorType errorType){
        this.code = errorType.getCode();
        this.message = errorType.getMessage();
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }
}
