package com.tencent.wxcloudrun.core;

import com.tencent.wxcloudrun.core.exception.GlobalException;
import com.tencent.wxcloudrun.util.ValidationUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Map;

/**
 * @author tu
 * @date 2022-05-12 17:36:04
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);




    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Result<Map<String,String>> result = handleBaseException(e);
        result.setRetCode(HttpStatus.BAD_REQUEST.value());
        result.setMsg("字段校验错误,请完善后重试");
        Map<String, String> errMap = ValidationUtils.mapWithFieldError(e.getBindingResult().getFieldErrors());
        result.setData(errMap);
        return result;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodBindException(BindException e){
        Result<Map<String,String>> result = handleBaseException(e);
        result.setRetCode(HttpStatus.BAD_REQUEST.value());
        result.setMsg("字段校验错误,请完善后重试");
        Map<String, String> errMap = ValidationUtils.mapWithFieldError(e.getBindingResult().getFieldErrors());
        result.setData(errMap);
        return result;
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        Result<?> result = handleBaseException(e);
        result.setMsg(String.format("请求字段缺失, 类型 %s, 名称 %s", e.getParameterType(), e.getParameterName()));
        return result;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e) {
        Result<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setRetCode(HttpStatus.BAD_REQUEST.value());
        baseResponse.setMsg("字段验证错误，请完善后重试！");
        baseResponse.setData(ValidationUtils.mapWithValidError(e.getConstraintViolations()));
        return baseResponse;
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Result<?> handleHttpMediaTypeNotAcceptableException(
            HttpMediaTypeNotAcceptableException e) {
        Result<?> baseResponse = handleBaseException(e);
        baseResponse.setRetCode(HttpStatus.NOT_ACCEPTABLE.value());
        return baseResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        Result<?> baseResponse = handleBaseException(e);
        baseResponse.setRetCode(HttpStatus.BAD_REQUEST.value());
        baseResponse.setMsg("缺失请求主体");
        return baseResponse;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Result<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        Result<?> baseResponse = handleBaseException(e);
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        baseResponse.setRetCode(status.value());
        baseResponse.setMsg(status.getReasonPhrase());
        return baseResponse;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleUploadSizeExceededException(MaxUploadSizeExceededException e) {
        Result<Object> response = handleBaseException(e);
        response.setRetCode(HttpStatus.BAD_REQUEST.value());
        response.setMsg("当前请求超出最大限制：" + e.getMaxUploadSize() + " bytes");
        return response;
    }

    @ExceptionHandler(GlobalException.class)
    public Result<?> handleHaloException(GlobalException e) {
        Result<Object> baseResponse = handleBaseException(e);
        baseResponse.setRetCode(e.getCode());
        baseResponse.setMsg(e.getMsg());
        return baseResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleGlobalException(Exception e) {
        Result<?> baseResponse = handleBaseException(e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        baseResponse.setRetCode(status.value());
        baseResponse.setMsg(status.getReasonPhrase());
        return baseResponse;
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        Result<?> baseResponse = handleBaseException(e);
        baseResponse.setRetCode(HttpStatus.BAD_REQUEST.value());
        return baseResponse;
    }
    
    
    

    private <T> Result<T> handleBaseException(Throwable t) {


        Assert.notNull(t, "Throwable must not be null");

        Result<T> result = new Result<>();
        result.message(t.getMessage());
        if(log.isDebugEnabled()){
            t.printStackTrace();
            result.setDevMsg(ExceptionUtils.getStackTrace(t));
        }
        return result;
    }
}
