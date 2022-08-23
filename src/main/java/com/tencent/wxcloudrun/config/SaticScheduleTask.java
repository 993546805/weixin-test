package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.service.WxTemplateMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author tu
 * @date 2022-08-23 14:39:13
 */
@Configuration
@EnableScheduling
public class SaticScheduleTask {

    private final Logger log = LoggerFactory.getLogger(SaticScheduleTask.class);

    @Autowired
    private WxTemplateMessageSender wxTemplateMessageSender;

    @Scheduled(cron = "0 0 9 * * *")
//    @Scheduled(cron = "0 0/2 * * * *")
    private void sendTemplateMessage(){
            wxTemplateMessageSender.sendDailyMessage();
    }
}
