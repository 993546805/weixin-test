package com.tencent.wxcloudrun.core.exception;



/**
 * 全局异常处理
 *
 * @author tu
 * @date 2022-05-10 8:17 下午
 */
public class GlobalException extends RuntimeException{
    private String msg;
    private int code = 600;



    public GlobalException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public GlobalException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
