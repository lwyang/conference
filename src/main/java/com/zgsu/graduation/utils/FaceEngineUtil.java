package com.zgsu.graduation.utils;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/7 20:31
 */
public class FaceEngineUtil {
    public static FaceEngine initEngine() {

       // String appId = "EYasL2ZscmdxAn76YqB3NxVpXFWapNY4Cz9jA7DNeHV8";
        //String sdkKey = "CMneYzqghsWAXK9i43A5Lmq33oW46waR8LZGHucVyChG";

        FaceEngine faceEngine = new FaceEngine("D:\\arcsoft_lib");
        //String path=System.getProperty("user.dir");
        //FaceEngine faceEngine = new FaceEngine(path+" \\src\\main\\resources\\arcsoft_lib");

        //激活引擎
//         int activeCode = faceEngine.activeOnline(appId, sdkKey);
//
//         if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
//            System.out.println("引擎激活失败");
//        }


        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);


        //初始化引擎
        int initCode = faceEngine.init(engineConfiguration);

        if (initCode != ErrorInfo.MOK.getValue()) {
            System.out.println("初始化引擎失败");
        }
        return faceEngine;
    }

    /**
     * 对边两个人脸特征值
     * @param faceEngine
     * @param targetFeatureData
     * @param sourceFeatureData
     * @return 相似度
     */
    public static float faceCompany(FaceEngine faceEngine, byte[] targetFeatureData, byte[] sourceFeatureData) {
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(targetFeatureData);
        FaceFeature sourceFaceFeature = new FaceFeature();
        sourceFaceFeature.setFeatureData(sourceFeatureData);
        FaceSimilar faceSimilar = new FaceSimilar();
        faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        return faceSimilar.getScore();
    }
}
