package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.Vo.UserInfoVo;
import com.zgsu.graduation.model.ConferenceAppointment;
import com.zgsu.graduation.service.ConferenceAppointmentService;
import com.zgsu.graduation.service.ConferenceParticipantService;
import com.zgsu.graduation.service.RoomService;
import com.zgsu.graduation.service.UserService;
import com.zgsu.graduation.utils.FaceEngineUtil;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class FaceController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ConferenceAppointmentService conferenceAppointmentService;
    @Autowired
    private ConferenceParticipantService conferenceParticipantService;

    @ApiOperation(value = "门禁人脸识别")
    @PostMapping("/face")
    public ResultVo faceCompare(@RequestParam("faceFeature") byte[] faceFeature, @RequestParam("serialNumber") String serialNumber) {
        List<Map<String, Object>> faceInfoList = userService.showFaceInfo();
        Map<String, Object> result = new HashMap<>();
        for (Map<String, Object> map : faceInfoList) {
            Float companyResult = FaceEngineUtil.faceCompany(FaceEngineUtil.initEngine(), faceFeature, (byte[]) map.get("face_feature"));
            if (companyResult >= 0.8) {
                //人脸识别为管理员，打开门禁（管理员可打开所有门禁）
                if (map.get("role").equals("管理员")) {
                    result.put("role", "管理员");
                    result.put("pass", true);
                    result.put("message", "管理员可打开所有门禁");
                    return ResultMsgUtil.success(result);
                    //人脸识别结果为用户，进一步判断
                } else {
                    //获取用户id
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
                        List<Integer> participantList = conferenceParticipantService.selectByConferenceId(conferenceId);
                        for(Integer participant: participantList){
                            if(participant.equals(userId)){
                                result.put("role", "用户");
                                result.put("pass", true);
                                result.put("message", "人脸识别成功");
                                return ResultMsgUtil.success(result);
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
}
