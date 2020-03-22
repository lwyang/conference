package com.zgsu.graduation.service;

import com.zgsu.graduation.enums.ErrorEnum;

import java.util.List;

public interface ConferenceParticipantService {
    ErrorEnum inviteParticipants(Integer conconferenceId, Integer initiatorId, List<Integer> participants);

    List<Integer> selectByConferenceId(Integer conferenceId);
}
