package com.zgsu.graduation.service;

import com.zgsu.graduation.Vo.AppointmentTimeVo;

import com.zgsu.graduation.model.ConferenceAppointment;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public interface ConferenceAppointmentService {
    /**
     * 预约会议室
     * @param conferenceAppointment
     * @return
     */
    Integer conferenceBook(ConferenceAppointment conferenceAppointment);

    /**
     * 根据会议室id和日期查看该会议室该天预约时间段
     * @return
     */
    List<AppointmentTimeVo> showAppointmentTime(Integer id, String date) ;

    List<ConferenceAppointment> selectByTimeAndRoomId(Integer roomId, String date, Time time);



}
