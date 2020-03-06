package com.zgsu.graduation.Vo;

import lombok.Data;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/7 9:53
 */
@Data
public class ResultVo<T> {
    /**错误码*/
    private Integer code;
    /**提示信息**/
    private String msg;
    /**返回数据**/
    private T data;
}
