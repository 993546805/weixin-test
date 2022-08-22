package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.service.WxApi;
import com.tencent.wxcloudrun.service.WxTemplateMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tu
 * @date 2022-08-22 14:28:32
 */
@Service
public class WxTemplateMessageSenderImpl implements WxTemplateMessageSender {

    @Autowired
    private WxApi wxApi;

    private String templateId = "7lxin4OK9Np8OvA41304Wpaw9sJuybLIvf57cHyGgkQ";

    @Override
    public void send(Map<String, String> messageParam) {
        Map<String,Object> dataMap = buildData(messageParam);
        List<String> openIdList = wxApi.getWatchOpenIdList();
        for (String openId : openIdList) {
            Map<String,Object> messageBody = buildMessageBody(dataMap, openId, templateId, "#FF0000");
            wxApi.sendTemplateMessage(messageBody);
        }
    }

    @Override
    public synchronized void changeTemplateId(String templateId) {
        this.templateId = templateId;
    }

    private Map<String, Object> buildMessageBody(Map<String, Object> dataMap, String openId, String templateId, String topColor) {
        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("touser", openId);
        messageBody.put("template_id", templateId);
        messageBody.put("topcolor", topColor);
        messageBody.put("data", dataMap);
        return messageBody;
    }

    private Map<String, Object> buildData(Map<String, String> messageParam) {
        Map<String,Object> dataMap = new HashMap<>();
        for (Map.Entry<String, String> stringStringEntry : messageParam.entrySet()) {
            Map<String,Object> value = new HashMap<>();
            value.put("value", stringStringEntry.getValue());
            value.put("color", "#c3c3c3");
            dataMap.put(stringStringEntry.getKey(), value);
        }
        return dataMap;
    }
}
