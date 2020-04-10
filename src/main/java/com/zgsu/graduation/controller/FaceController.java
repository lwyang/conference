package com.zgsu.graduation.controller;

import com.arcsoft.face.FaceEngine;
import com.zgsu.graduation.Vo.FaceInfoVo;
import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.ConferenceAppointment;
import com.zgsu.graduation.service.*;
import com.zgsu.graduation.utils.FaceEngineUtil;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class FaceController {
    private static final Integer SIGIN_TRUE = 3;//已签到
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ConferenceAppointmentService conferenceAppointmentService;
    @Autowired
    private ConferenceParticipantService conferenceParticipantService;
    @Autowired
    private FaceService faceService;


    @ApiOperation(value = "门禁人脸识别")
    @PostMapping("/face")
    public ResultVo faceCompare(@RequestBody FaceInfoVo faceInfoVo) {
        //@RequestParam("faceFeature") String faceFeature, @RequestParam(value = "serialNumber",required = false) String serialNumber
        byte[] faceFeatures = faceInfoVo.getFaceFeature();
        String serialNumber = faceInfoVo.getSerialNumber();
        //byte[] faceFeatures = faceFeature.getBytes();
        //System.out.println("长度"+faceFeatures.length+"---"+faceFeatures.toString());
        List<Map<String, Object>> faceInfoList = userService.showFaceInfo();
        Map<String, Object> result = new HashMap<>();
        FaceEngine faceEngine = FaceEngineUtil.initEngine();
        for (Map<String, Object> map : faceInfoList) {
            //System.out.println(Arrays.toString((byte[]) map.get("face_feature")));
            // System.out.println(((byte[]) map.get("face_feature")).length);
            Float companyResult = FaceEngineUtil.faceCompany(faceEngine, faceFeatures, (byte[]) map.get("face_feature"));
            //System.out.println("相似度" + companyResult);
            if (companyResult >= 0.8) {
                result.put("userId", map.get("id"));
                //人脸识别为管理员，打开门禁（管理员可打开所有门禁）
                if (map.get("role").equals("管理员")) {
                    result.put("role", "管理员");
                    result.put("pass", true);
                    result.put("message", "管理员可打开所有门禁");
                    return ResultMsgUtil.success(result);
                    //人脸识别结果为用户，进一步判断
                } else {
                    //获取人脸比对成功者的id
                    Integer userId = (Integer) map.get("id");
                    //获取该设备对应会议室id
                    Integer roomId = roomService.selectIdBySerialNumber(serialNumber);
                    //获取系统日期
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Time now = new Time(System.currentTimeMillis());
                    //查询该会议室现在是否有会议进行
                    List<ConferenceAppointment> conferenceAppointmentList = conferenceAppointmentService.selectByTimeAndRoomId(roomId, simpleDateFormat.format(date), now);
                    //此刻无会议进行
                    if (conferenceAppointmentList.size() == 0) {
                        result.put("role", "用户");
                        result.put("pass", false);
                        result.put("message", "此刻暂无您的会议安排，无法进入");
                        return ResultMsgUtil.success(result);
                        //此刻有会议进行，进一步判断扫脸者是否为参会者
                    } else {
                        //获取会议id
                        Integer conferenceId = conferenceAppointmentList.get(0).getId();
                        //根据y会议id查找会议发起者id
                        Integer initiatorId = conferenceAppointmentService.showInitiatorIdByConferenceId(conferenceId);
                        //根据会议id获取参会者
                        List<Integer> participantList = conferenceParticipantService.selectByConferenceId(conferenceId);
                        for (Integer participant : participantList) {
                            if (participant.equals(userId)) {
                                result.put("role", "用户");
                                result.put("pass", true);
                                //若用户首次扫脸，提示签到成功
                                if (conferenceParticipantService.showState(conferenceId, participant) != SIGIN_TRUE) {
                                    Integer siginResult = conferenceParticipantService.updateState(conferenceId, participant, SIGIN_TRUE);
                                    //数据库状态修改失败
                                    if (siginResult == 0) {
                                        result.put("message", "数据库状态修改失败");
                                    } else {
                                        result.put("message", "签到成功，请进入");
                                    }
                                    return ResultMsgUtil.success(result);
                                    //若已签到，提示已签到
                                } else {
                                    if (userId.equals(initiatorId)) {
                                        result.put("message", "是否签退");
                                    } else {
                                        result.put("message", "您已经签到过了，请进入");
                                    }
                                    return ResultMsgUtil.success(result);
                                }

                            }
                        }
                        result.put("role", "用户");
                        result.put("pass", false);
                        result.put("message", "您不是本次会议参与者，无法进入");
                        return ResultMsgUtil.success(result);
                    }

                }
            }
        }
        result.put("role", "暂无信息");
        result.put("pass", false);
        result.put("message", "系统无法查找到您的信息");
        Calendar calendar = Calendar.getInstance();
        // UserInfoVo userInfoVo = userService.showUser(12);
        // Float result = FaceEngineUtil.faceCompany(FaceEngineUtil.initEngine(), faceFeature, userInfoVo.getFaceFeature());
        return ResultMsgUtil.success(result);
    }

    @ApiOperation(value = "发起者会议签退")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "conferenceId", value = "会议id", required = true, paramType = "query")
    })
    @PostMapping("/signOff")
    public ResultVo signOff(@RequestParam("userId") Integer userId, @RequestParam("conferenceId") Integer conferenceId) {
        ErrorEnum errorEnum = faceService.signOff(userId, conferenceId);
        if (errorEnum.getCode() == 200) {
            return ResultMsgUtil.success();
        }
        return ResultMsgUtil.error(errorEnum.getCode(), errorEnum.getMsg());
    }
}
