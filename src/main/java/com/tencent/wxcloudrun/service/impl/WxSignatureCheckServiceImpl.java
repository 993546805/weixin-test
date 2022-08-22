package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.service.WxSignatureCheckService;
import com.tencent.wxcloudrun.util.SHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tu
 * @date 2022-08-22 10:48:08
 */
@Service
public class WxSignatureCheckServiceImpl implements WxSignatureCheckService {

    private final Logger log = LoggerFactory.getLogger(WxSignatureCheckServiceImpl.class);

    @Value("${wx.token}")
    private String token;

    @Override
    public String check(String signature, String timestamp, String nonce, String echostr) {
        String parsedToken = SHA1.getSHA1(token, timestamp, nonce, echostr);

        log.info("token: [{}]", token);
        log.info("signature:[{}] ,timestamp:[{}] ,nonce:[{}], echostr:[{}]", signature, timestamp, nonce, echostr);
        log.info("parsedToken: [{}]", parsedToken);


        if (!"".equals(parsedToken) && parsedToken.equals(signature)) {
            log.info("验签通过");
            return echostr;
        } else {
            log.info("验签失败");
            return echostr;
        }

    }
}
