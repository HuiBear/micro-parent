package com.micro.ykh.common.exception;

import com.micro.ykh.domain.AjaxResult;
import com.micro.ykh.exception.CustomException;
import com.micro.ykh.utils.text.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author xiongh
 * @Date 2021/1/9 10:39
 * @Version 1.0
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {CustomException.class})
    public AjaxResult handleCustomException(CustomException e) {
        if (StringUtils.isNull(e.getCode())) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public AjaxResult handleBindException(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        List<ObjectError> objectErrorList = e.getBindingResult().getAllErrors();
        for (ObjectError error : objectErrorList) {
            if (objectErrorList.size() - 1 == objectErrorList.indexOf(error)) {
                errorMsg.append(error.getDefaultMessage());
            } else {
                errorMsg.append(error.getDefaultMessage()).append(",");
            }
        }
        return AjaxResult.error(errorMsg.toString());
    }

//    @ExceptionHandler(Throwable.class)
//    public AjaxResult handleError(Throwable e) {
//        logger.error(e.getMessage());
//        return AjaxResult.error(e.getMessage());
//    }
}
