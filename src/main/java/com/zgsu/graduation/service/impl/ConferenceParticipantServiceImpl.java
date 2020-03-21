package com.zgsu.graduation.service.impl;

import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.ConferenceParticipantMapper;
import com.zgsu.graduation.model.ConferenceParticipant;
import com.zgsu.graduation.service.ConferenceParticipantService;
import io.goeasy.GoEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ConferenceParticipantServiceImpl implements ConferenceParticipantService {
    @Autowired
    private ConferenceParticipantMapper conferenceParticipantMapper;


    @Override
    public ErrorEnum inviteParticipants( Integer conconferenceId, List<Integer> participants) {
        for(Integer participant:participants){
            ConferenceParticipant conferenceParticipant=new ConferenceParticipant();
            conferenceParticipant.setConferenceId(conconferenceId);
            conferenceParticipant.setParticipant(participant);
            int result=conferenceParticipantMapper.insertSelective(conferenceParticipant);
            if(result==0){
                return ErrorEnum.FAILURE;
            }
        }
        GoEasy goEasy=new GoEasy("http://rest-hangzhou.goeasy.io/publish","BC-fd94510bceb2485fa37fe7caaeb06b27");
        goEasy.publish(Integer.toString(conconferenceId),"邀请你参与会议");
        return ErrorEnum.SUCCESS;
    }
}

