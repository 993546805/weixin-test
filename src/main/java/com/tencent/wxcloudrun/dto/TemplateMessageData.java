package com.tencent.wxcloudrun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tu
 * @date 2022-08-23 17:02:48
 */
@Data
@AllArgsConstructor
public class TemplateMessageData {
    private String value;
    private String color;
}
