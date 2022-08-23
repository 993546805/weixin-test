package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.service.WxApi;
import com.tencent.wxcloudrun.service.WxSignatureCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author tu
 * @date 2022-08-22 10:39:24
 */
@RestController
public class WxSignatureCheckController {

    private final WxSignatureCheckService wxSignatureCheckService;

    @Autowired
    private WxApi wxApi;

    public WxSignatureCheckController(@Autowired WxSignatureCheckService wxSignatureCheckService) {
        this.wxSignatureCheckService = wxSignatureCheckService;
    }

    @GetMapping("/wx_check")
    public String wxSignatureCheck(@RequestParam(value = "signature",required = false) String signature
            , @RequestParam(value = "timestamp",required = false) String timestamp
            , @RequestParam(value = "nonce",required = false) String nonce
            , @RequestParam(value = "echostr",required = false) String echostr) {
        return wxSignatureCheckService.check(signature, timestamp, nonce, echostr);
    }

    @PostMapping("/wx_check")
    public void onMessage(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        String responseMessage = wxApi.handleMessage(request);
        out.print(responseMessage);
        out.flush();
    }



}
