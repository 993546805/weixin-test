package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.service.WxSignatureCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tu
 * @date 2022-08-22 10:39:24
 */
@RestController
public class WxSignatureCheckController {

    private final WxSignatureCheckService wxSignatureCheckService;

    public WxSignatureCheckController(@Autowired WxSignatureCheckService wxSignatureCheckService) {
        this.wxSignatureCheckService = wxSignatureCheckService;
    }

    @RequestMapping("/wx_check")
    public String wxSignatureCheck(@RequestParam(value = "signature",required = false) String signature
            , @RequestParam(value = "timestamp",required = false) String timestamp
            , @RequestParam(value = "nonce",required = false) String nonce
            , @RequestParam(value = "echostr",required = false) String echostr) {
        return wxSignatureCheckService.check(signature, timestamp, nonce, echostr);
    }



}
