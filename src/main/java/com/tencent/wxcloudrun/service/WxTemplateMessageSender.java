package com.tencent.wxcloudrun.service;


import java.util.Map;

/**
 * @author tu
 */
public interface WxTemplateMessageSender {

    void send(Map<String, String> messageParam);

    void changeTemplateId(String templateId);

    void sendDailyMessage();

}
