package com.tencent.wxcloudrun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @author tu
 * @date 2022-08-23 14:39:13
 */
@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Scheduled(cron = "0 0 15 ? * *")
    private void sendTemplateMessage(){
        System.out.println(new Date());
    }
}
