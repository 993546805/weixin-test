package com.tencent.wxcloudrun.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tu
 */
@Data
@AllArgsConstructor
public class Result<T> {
    private int retCode;
    private String msg;
    private T data;
    private String errorUrl;

    /**
     * 成功返回的默认状态码
     */
    private static final int DEFAULT_SUCCESS_CODE = 200;
    /**
     * 错误返回的默认状态码
     */
    private static final int DEFAULT_ERROR_CODE = 600;

    /**
     * 成功返回的默认消息
     */
    private static final String DEFAULT_SUCCESS_MESSAGE = "成功";
    /**
     * 失败返回的默认消息
     */
    private static final String DEFAULT_ERROR_MESSAGE = "失败";
    private String devMsg;

    public Result(){
        this.retCode = DEFAULT_SUCCESS_CODE;
        this.msg = DEFAULT_SUCCESS_MESSAGE;
    }


    public Result(T data, int retCode, String msg) {
        this.data = data;
        this.retCode = retCode;
        this.msg = msg;
    }




    public Result<T> ok(){
        return this;
    }

    public Result<T> error(){
        setRetCode(DEFAULT_ERROR_CODE);
        setMsg(DEFAULT_ERROR_MESSAGE);
        return this;
    }




    public Result<T> code(int code){
        this.retCode = code;
        return this;
    }

    public Result<T> message(String message){
        this.setMsg(message);
        return this;
    }

    public Result<T> data(T data){
        this.setData(data);
        return this;
    }

    public void setDevMsg(String devMsg) {
        this.devMsg = devMsg;
    }

    public String getDevMsg() {
        return devMsg;
    }
}
