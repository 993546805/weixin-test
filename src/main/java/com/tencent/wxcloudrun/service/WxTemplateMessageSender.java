package com.tencent.wxcloudrun.service;


import com.tencent.wxcloudrun.dto.TemplateMessageData;

import java.util.Map;

/**
 * @author tu
 */
public interface WxTemplateMessageSender {

    void send(Map<String, TemplateMessageData> messageParam);

    void changeTemplateId(String templateId);

    void sendDailyMessage();


    String getLoveDescription(String content);

}
