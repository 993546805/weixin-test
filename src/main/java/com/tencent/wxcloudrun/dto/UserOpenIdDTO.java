package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.util.List;

/**
 * @author tu
 * @date 2022-08-22 15:32:58
 */
@Data
public class UserOpenIdDTO {
    private Integer total;
    private Integer count;
    private DataDTO data;
    private String next_openid;

    @Data
    public static class DataDTO {
        private List<String> openid;
    }
}
