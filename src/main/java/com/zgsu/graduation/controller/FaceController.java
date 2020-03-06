package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.Vo.UserInfoVo;
import com.zgsu.graduation.model.UserInfo;
import com.zgsu.graduation.service.UserService;
import com.zgsu.graduation.utils.FaceEngineUtil;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaceController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "人脸特征码比对测试")
    @GetMapping("/face")
    public ResultVo faceCompare(@RequestParam("faceFeature") byte[] faceFeature) {
        UserInfoVo userInfoVo = userService.showUser(12);
        Float result = FaceEngineUtil.faceCompany(FaceEngineUtil.initEngine(), faceFeature, userInfoVo.getFaceFeature());
        return ResultMsgUtil.success(result);
    }
}
