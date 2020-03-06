package com.zgsu.graduation.service;

import com.zgsu.graduation.enums.ErrorEnum;

import java.util.List;

public interface ConferenceParticipantService {
    ErrorEnum inviteParticipants(Integer conconferenceId, List<Integer> participants);
}
