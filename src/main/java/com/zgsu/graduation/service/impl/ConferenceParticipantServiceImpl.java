package com.zgsu.graduation.service.impl;

import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.ConferenceParticipantMapper;
import com.zgsu.graduation.model.ConferenceParticipant;
import com.zgsu.graduation.service.ConferenceParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ErrorEnum.SUCCESS;
    }
}
