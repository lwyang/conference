package com.zgsu.graduation.service;
import com.zgsu.graduation.Vo.RoomVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.AppointmentConditions;

import java.text.ParseException;
import java.util.List;

public interface ConferenceRcdService {
    /**
     * 根据条件推荐适合会议室
     * @return
     */
    List<RoomVo> conferenceCommend(AppointmentConditions appointmentConditions) throws ParseException;

    ErrorEnum addRcdConditions(AppointmentConditions appointmentConditions);
}
