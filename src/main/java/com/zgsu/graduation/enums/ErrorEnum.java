package com.zgsu.graduation.enums;

import lombok.Getter;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/8 10:42
 */
@Getter
public enum ErrorEnum {
    SUCCESS(200, "成功"),
    REPEAT(1, "人脸注册重复"),
    FAILURE(2, "操作失败"),
    UPDATE_FAILURE(3,"更新信息失败"),
    NO_DEPARTMENT(4,"部门不存在"),
    DELETE_FAILURE(5,"删除失败"),
    ADD_FAILURE(6,"添加部门失败");
    private Integer code;
    private String msg;

    ErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
