package com.tencent.wxcloudrun.service;


/**
 * 微信验签接口
 * @author tu
 */
public interface WxSignatureCheckService {

    /**
     * 接口验签
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    String check(String signature, String timestamp, String nonce, String echostr);
}
