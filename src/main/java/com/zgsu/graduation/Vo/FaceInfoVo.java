package com.zgsu.graduation.Vo;

import lombok.Data;

/**
 * 用于门禁人脸识别
 */
@Data
public class FaceInfoVo {
    //人脸特征值
    private byte[] faceFeature;
    //设备序列号
    private String serialNumber;
}
