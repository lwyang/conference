package com.zgsu.graduation.service.impl;

import com.zgsu.graduation.Vo.AppointmentTimeVo;
import com.zgsu.graduation.mapper.ConferenceAppointmentMapper;
import com.zgsu.graduation.mapper.ConferenceParticipantMapper;
import com.zgsu.graduation.model.ConferenceAppointment;
import com.zgsu.graduation.service.ConferenceAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class ConferenceAppointmentServiceImpl implements ConferenceAppointmentService {
    @Autowired
    private ConferenceAppointmentMapper conferenceAppointmentMapper;
    @Autowired
    private ConferenceParticipantMapper conferenceParticipantMapper;

    @Override
    public Integer conferenceBook(ConferenceAppointment conferenceAppointmentk) {
        int result = conferenceAppointmentMapper.insertSelective(conferenceAppointmentk);
        return result == 1 ? conferenceParticipantMapper.selectLastId() : 0;
    }

    @Override
    public List<AppointmentTimeVo> showAppointmentTime(Integer id, String date) {
        Time now = new Time(System.currentTimeMillis());
        return conferenceAppointmentMapper.showAppointMentTime(id, date,now);
    }

    @Override
    public List<ConferenceAppointment> selectByTimeAndRoomId(Integer roomId, String date, Time time) {
        return conferenceAppointmentMapper.selectByTimeAndRoomId(roomId,date,time);
    }


}
