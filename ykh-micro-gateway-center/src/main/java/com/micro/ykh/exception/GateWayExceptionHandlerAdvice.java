package com.micro.ykh.exception;


import io.netty.channel.ConnectTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.SignatureException;

/**
 * @Author xiongh
 * @Description // 全局处理拦截
 * @Date 2021/1/9 11:28
 **/
@Component
public class GateWayExceptionHandlerAdvice {

    private static Logger logger = LogManager.getLogger(GateWayExceptionHandlerAdvice.class);

    @ExceptionHandler(value = {ResponseStatusException.class})
    public Result handle(ResponseStatusException ex) {
        logger.error("response status exception:{}", ex.getMessage());
        return Result.fail(SystemErrorType.GATEWAY_ERROR);
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    public Result handle(ConnectTimeoutException ex) {
        logger.error("connect timeout exception:{}", ex.getMessage());
        return Result.fail(SystemErrorType.GATEWAY_CONNECT_TIME_OUT);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handle(NotFoundException ex) {
        logger.error("not found exception:{}", ex.getMessage());
        return Result.fail(SystemErrorType.GATEWAY_NOT_FOUND_SERVICE);
    }

/*    @ExceptionHandler(value = {ExpiredJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handle(ExpiredJwtException ex) {
        logger.error("ExpiredJwtException:{}", ex.getMessage());
        return Result.fail(SystemErrorType.INVALID_TOKEN);
    }*/

    @ExceptionHandler(value = {SignatureException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handle(SignatureException ex) {
        logger.error("SignatureException:{}", ex.getMessage());
        return Result.fail(SystemErrorType.INVALID_TOKEN);
    }

/*    @ExceptionHandler(value = {MalformedJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result handle(MalformedJwtException ex) {
        logger.error("MalformedJwtException:{}", ex.getMessage());
        return Result.fail(SystemErrorType.INVALID_TOKEN);
    }*/

/*    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(RuntimeException ex) {
        logger.error("runtime exception:{}", ex.getMessage());
        return Result.fail();
    }*/

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handle(Exception ex) {
        logger.error("exception:{}", ex.getMessage());
        return Result.fail();
    }

    @ExceptionHandler(value = {GateWayException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result handle(GateWayException ex) {
        logger.error("exception:{}", ex);
        return Result.fail(ex.getCode(), ex.getMsg());
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handle(Throwable throwable) {
        Result result = Result.fail();
        if (throwable instanceof ResponseStatusException) {
            result = handle((ResponseStatusException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            result = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof NotFoundException) {
            result = handle((NotFoundException) throwable);
        } else if (throwable instanceof GateWayException) {
            result = handle((GateWayException) throwable);
        }else{
            logger.error("throwable exception:{}", throwable.getMessage());
        }
        return result;
    }
}
