package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.ConferenceAppointment;
import com.zgsu.graduation.service.ConferenceAppointmentService;
import com.zgsu.graduation.service.ConferenceParticipantService;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ConferenceAppointmentController {
    @Autowired
    private ConferenceAppointmentService conferenceAppointmentService;
    @Autowired
    private ConferenceParticipantService conferenceParticipantService;

    @ApiOperation(value = "预约会议")
    @PostMapping("/book")
    public ResultVo conferenceBook(@RequestBody ConferenceAppointment conferenceAppointment) {
        Integer result = conferenceAppointmentService.conferenceBook(conferenceAppointment);
        if (result == 0) {
            return ResultMsgUtil.error(500,"预约会议室失败");
        } else {
            Map<String,Object> map=new HashMap<>();
            map.put("conferenceId",result);
            return ResultMsgUtil.success(map);
        }

    }

    @ApiOperation("邀请参会者")
    @PostMapping("/participants")
    public ResultVo inviteParticipants(@RequestParam("conferenceId") Integer conconferenceId,
                                       @RequestParam("initiatorId") Integer initiatorId,
                                       @RequestParam("participants") List<Integer> participants) {
        ErrorEnum errorEnum = conferenceParticipantService.inviteParticipants(conconferenceId, initiatorId,participants);
        if (errorEnum.getCode().equals(200)) {
            return ResultMsgUtil.success();
        } else {
            return ResultMsgUtil.error(errorEnum.getCode(), errorEnum.getMsg());
        }

    }

}
