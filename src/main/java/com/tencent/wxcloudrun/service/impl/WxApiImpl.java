package com.tencent.wxcloudrun.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tencent.wxcloudrun.dto.TextMessage;
import com.tencent.wxcloudrun.dto.UserOpenIdDTO;
import com.tencent.wxcloudrun.service.MailService;
import com.tencent.wxcloudrun.service.WxApi;
import com.tencent.wxcloudrun.service.WxTemplateMessageSender;
import com.tencent.wxcloudrun.util.JsonUtils;
import com.tencent.wxcloudrun.util.WechatMessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author tu
 * @date 2022-08-22 15:04:51
 */
@Service
public class WxApiImpl implements WxApi {

    private final Logger log = LoggerFactory.getLogger(WxApiImpl.class);
    @Autowired
    private RestTemplate restTemplate;

    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.secret}")
    private String secret;

    private volatile String accessToken;
    private Date accessTokenExpiredTime;


    @Autowired
    private WxTemplateMessageSender wxTemplateMessageSender;
    @Autowired
    private MailService mailService;


    private static final Map<String, String> URL_MAP = new HashMap<>();

    static {
        URL_MAP.put("access_token", "https://api.weixin.qq.com/cgi-bin/token");
        URL_MAP.put("user_list", "https://api.weixin.qq.com/cgi-bin/user/get?access_token=#{ACCESS_TOKEN}");
        URL_MAP.put("send_template", "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=#{ACCESS_TOKEN}");
    }

    @Override
    public String getAccessToken() {
        if (accessTokenExpiredTime == null || accessTokenExpiredTime.before(new Date())) {
            HashMap<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("grant_type", "client_credential");
            uriVariables.put("appid", appid);
            uriVariables.put("secret", secret);
            String url = URL_MAP.get("access_token");
            url = url + "?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            String accessToken = (String) response.get("access_token");
            if (StringUtils.isEmpty(accessToken)) {
                throw new RuntimeException("获取 accessToken 失败");
            }
            this.accessToken = accessToken;
            Integer expiresMill = (Integer) response.get("expires_in");
            this.accessTokenExpiredTime = new Date(System.currentTimeMillis() + expiresMill * 1000);
            log.info("accessToken: [{}], expiredTime: [{}],nowTime: [{}]", accessToken, accessTokenExpiredTime, new Date());
            return accessToken;
        } else {
            return accessToken;
        }

    }

    @Override
    public void sendTemplateMessage(Map<String, Object> messageBody) {

        String url = URL_MAP.get("send_template");
        String realUrl = url.replace("#{ACCESS_TOKEN}", getAccessToken());

        log.info("send template message body : [{}]", messageBody);
        Map responseMap = restTemplate.postForObject(realUrl, messageBody, Map.class);
        try {
            if (responseMap != null) {
                String response = JsonUtils.objectToJson(responseMap);
                log.info("response: [{}]", response);
            } else {
                log.info("未获取到返回值.");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private Map<String, String> getAccessTokenMap() {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", getAccessToken());
        return map;
    }

    @Override
    public List<String> getWatchOpenIdList() {
        String userListUrl = URL_MAP.get("user_list");
        String realUrl = userListUrl.replace("#{ACCESS_TOKEN}", getAccessToken());
        UserOpenIdDTO userOpenIdDTO = restTemplate.getForObject(realUrl, UserOpenIdDTO.class);
        List<String> list = Optional.ofNullable(userOpenIdDTO).map(userOpenIdDTO1 -> userOpenIdDTO1.getData().getOpenid()).orElseThrow(() -> new RuntimeException("获取关注列表失败"));
        return list;
    }

    @Override
    public String handleMessage(HttpServletRequest request) {
        Map<String, String> map = WechatMessageUtil.xmlToMap(request);
        log.info("接收到的消息: [{}]", map);
        // 发送方帐号（一个OpenID）
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        // 消息类型
        String msgType = map.get("MsgType");
        String content = map.get("Content");


        // 默认回复一个"success"
        String responseMessage = "success";
        if (WechatMessageUtil.MESSAGE_TEXT.equals(msgType)) {
            TextMessage textMessage = new TextMessage();
            textMessage.setMsgType(WechatMessageUtil.MESSAGE_TEXT);
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(System.currentTimeMillis());
            String returnMessageByContent = getReturnMessageByContent(content);
            textMessage.setContent(returnMessageByContent);
            responseMessage = WechatMessageUtil.textMessageToXml(textMessage);

            try {
                mailService.sendContent(content,returnMessageByContent);
            } catch (Exception e) {
                log.error("发送邮件失败. 消息内容: [{}]", content);
            }
        }

        log.debug("发送的消息: [{}]", responseMessage);
        return responseMessage;
    }

    private String getReturnMessageByContent(String content) {

        if (content.contains("屠红斌") || content.contains("乖") || content.contains("宝贝")) {
            return "\uD83C\uDF37你是要和我表白嘛,表白的话就去私聊我吧~";
        }

        return wxTemplateMessageSender.getLoveDescription(content);
    }


}
