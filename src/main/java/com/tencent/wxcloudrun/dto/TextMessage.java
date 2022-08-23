package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * @author tu
 * @date 2022-08-23 14:01:44
 */
@Data
public class TextMessage {
    private String msgType;
    private String toUserName;
    private String fromUserName;
    private Long createTime;
    private String content;
}
