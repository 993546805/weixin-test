package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author tu
 * @date 2022-08-23 17:31:45
 */
@Service
public class MailServiceImpl implements MailService {

    private Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender sender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendContent(String content, String returnMessageByContent) {
        log.info("收到的消息: [{}],发送的消息: [{}]", content, returnMessageByContent);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo("993546805@qq.com");
        simpleMailMessage.setSubject("你家那位发的邮件来啦");
        simpleMailMessage.setText(content + "/n" + returnMessageByContent);
        sender.send(simpleMailMessage);
    }
}
