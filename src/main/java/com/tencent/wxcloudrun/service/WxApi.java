package com.tencent.wxcloudrun.service;

import java.util.List;
import java.util.Map;

/**
 * @author tu
 */
public interface WxApi {

    String getAccessToken();


    /**
     * 发送模板消息
     * @param messageBody 请求体
     */
    void sendTemplateMessage(Map<String, Object> messageBody);

    List<String> getWatchOpenIdList();

}
