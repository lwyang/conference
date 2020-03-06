package com.zgsu.graduation.enums;

import lombok.Getter;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/7 9:45
 */
@Getter
public enum ResultEnum {
    SUCCESS(200,"成功"),
    FAILURE(400,"失败");
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
