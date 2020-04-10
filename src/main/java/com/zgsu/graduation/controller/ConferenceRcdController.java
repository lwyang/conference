package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.AppointmentTimeVo;
import com.zgsu.graduation.Vo.RecommendVo;
import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.Vo.RoomVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.AppointmentConditions;
import com.zgsu.graduation.service.ConferenceAppointmentService;
import com.zgsu.graduation.service.ConferenceRcdService;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ConferenceRcdController {
    @Autowired
    private ConferenceRcdService conferenceRcdService;
    @Autowired
    private ConferenceAppointmentService conferenceAppointmentService;

    @ApiOperation(value = "会议室推荐")
    @PostMapping("/recommendation")
    public ResultVo recommend(@RequestBody AppointmentConditions appointmentConditions) throws ParseException {
        ErrorEnum errorEnum = conferenceRcdService.addRcdConditions(appointmentConditions);
        if (errorEnum.getCode().equals(2)) {
            return ResultMsgUtil.error(ErrorEnum.FAILURE.getCode(), "会议室推荐失败");
        }
        List<RoomVo> roomVoList = conferenceRcdService.conferenceCommend(appointmentConditions);
        List<RecommendVo> recommendVoList = new ArrayList<>();
        for (RoomVo roomVo : roomVoList) {
            RecommendVo recommendVo = new RecommendVo();
            recommendVo.setRoomVo(roomVo);
            List<AppointmentTimeVo> appointmentTimeVos = conferenceAppointmentService.showAppointmentTime(roomVo.getId(), appointmentConditions.getDate());
            recommendVo.setAppointmentTimeVo(appointmentTimeVos);
            recommendVoList.add(recommendVo);
        }
        return ResultMsgUtil.success(recommendVoList);

    }

    @ApiOperation(value = "会议室评价")
    @PostMapping("/evaluate")
    public ResultVo roomEvaluate(@RequestParam("conferenceId") Integer conferenceId,
                                 @RequestParam("participantId") Integer participantId,
                                 @RequestParam("mark") Float mark,
                                 @RequestParam(value = "evaluate", required = false) String evaluate) {
        if (evaluate == null) {
            evaluate = "暂无评价";
        }
        ErrorEnum errorEnum = conferenceRcdService.evaluateRoom(conferenceId, participantId, mark, evaluate);
        if (errorEnum.getCode().equals(200)) {
            return ResultMsgUtil.success(errorEnum.getMsg());
        } else {
            return ResultMsgUtil.error(errorEnum.getCode(), errorEnum.getMsg());
        }
    }
}
