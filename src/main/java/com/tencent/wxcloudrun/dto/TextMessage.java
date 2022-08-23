package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * @author tu
 * @date 2022-08-23 14:01:44
 */
@Data
public class TextMessage {
    private String MsgType;
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String Content;
}
