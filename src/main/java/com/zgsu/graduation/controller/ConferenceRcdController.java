package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.Vo.RoomVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.AppointmentConditions;
import com.zgsu.graduation.service.ConferenceRcdService;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class ConferenceRcdController {
    @Autowired
    private ConferenceRcdService conferenceRcdService;

    @ApiOperation(value = "会议室推荐")
    @PostMapping("/recommendation")
    public ResultVo recommend(@RequestBody AppointmentConditions appointmentConditions) throws ParseException {
        ErrorEnum errorEnum=conferenceRcdService.addRcdConditions(appointmentConditions);
        if(errorEnum.getCode().equals(2)){
            return ResultMsgUtil.error(ErrorEnum.FAILURE.getCode(),"会议室推荐失败");
        }
        List<RoomVo> roomVoList = conferenceRcdService.conferenceCommend(appointmentConditions);
        return ResultMsgUtil.success(roomVoList);
    }
}
