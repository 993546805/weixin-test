package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.service.WxTemplateMessageSender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author tu
 * @date 2022-08-22 14:25:32
 */
@RestController
public class WxTemplateMessageController {

    @Autowired
    private WxTemplateMessageSender wxTemplateMessageSender;

    @PostMapping("/send_template_message")
    public void sendMessage(@RequestBody Map<String, String> messageParam) {
        wxTemplateMessageSender.send(messageParam);
    }


    @GetMapping("/set_template_id/#{templateId}")
    public String setTemplateId(@PathVariable("templateId") String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            return "模板 ID 为空";
        }
        wxTemplateMessageSender.changeTemplateId(templateId);
        return "成功";
    }
}
