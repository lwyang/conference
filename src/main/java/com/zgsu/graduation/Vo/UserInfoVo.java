package com.zgsu.graduation.Vo;

import lombok.Data;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/8 19:00
 */
@Data
public class UserInfoVo {
    //用户id
    private Integer id;
    //部门id
    private Integer departmentId;
    //部门名
    private String department;
    //姓名
    private String name;
    //年龄
    private Integer age;

    private String email;
    //性别
    private Boolean gender;

    private String phone;
    //人脸特征值
    private byte[] faceFeature;


}
