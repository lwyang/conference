package com.zgsu.graduation.service;

import com.zgsu.graduation.enums.ErrorEnum;
import io.swagger.models.auth.In;

import java.util.List;

public interface ConferenceParticipantService {
    ErrorEnum inviteParticipants(Integer conconferenceId, Integer initiatorId, List<Integer> participants);

    List<Integer> selectByConferenceId(Integer conferenceId);
    //查询某人在某会议的签到情况
    Integer showState(Integer conferenceId,Integer participantId);

    //修改参态0代表未接受，1代表接受，2代表请假，3代表签到
    Integer updateState(Integer conferenceId,Integer participantId,Integer state);
}
