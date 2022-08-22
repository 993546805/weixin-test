package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.service.WxSignatureCheckService;
import com.tencent.wxcloudrun.util.Decript;
import com.tencent.wxcloudrun.util.SHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
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

        String sortString = sort(token, timestamp, nonce);

        String myToken = Decript.SHA1(sortString);



        if (!"".equals(myToken) && myToken.equals(signature)) {
            log.info("验签通过");
            return echostr;
        } else {
            log.info("验签失败");
            return null;
        }

    }

    private String sort(String token, String timestamp, String nonce) {
        return null;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
