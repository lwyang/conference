package com.zgsu.graduation.service.impl;

import com.zgsu.graduation.Vo.AllConferenceVo;
import com.zgsu.graduation.Vo.AppointmentTimeVo;
import com.zgsu.graduation.Vo.TodayConferenceVo;
import com.zgsu.graduation.mapper.ConferenceAppointmentMapper;
import com.zgsu.graduation.mapper.ConferenceParticipantMapper;
import com.zgsu.graduation.mapper.RoomMapper;
import com.zgsu.graduation.mapper.UserInfoMapper;
import com.zgsu.graduation.model.ConferenceAppointment;
import com.zgsu.graduation.model.ConferenceParticipant;
import com.zgsu.graduation.model.ConferenceParticipantExample;
import com.zgsu.graduation.model.Room;
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
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RoomMapper roomMapper;

    @Override
    public Integer conferenceBook(ConferenceAppointment conferenceAppointment) {
        int result = conferenceAppointmentMapper.insertSelective(conferenceAppointment);
        return result == 1 ? conferenceParticipantMapper.selectLastId() : 0;
    }

    @Override
    public List<AppointmentTimeVo> showAppointmentTime(Integer id, String date) {
        Time now = new Time(System.currentTimeMillis());
        return conferenceAppointmentMapper.showAppointMentTime(id, date, now);
    }

    @Override
    public List<ConferenceAppointment> selectByTimeAndRoomId(Integer roomId, String date, Time time) {
        return conferenceAppointmentMapper.selectByTimeAndRoomId(roomId, date, time);
    }

    @Override
    public Integer showInitiatorIdByConferenceId(Integer conferenceId) {
        ConferenceAppointment conferenceAppointment = conferenceAppointmentMapper.selectByPrimaryKey(conferenceId);
        Integer initiatorId = conferenceAppointment.getConferenceInitiator();
        return initiatorId;
    }

    @Override
    public Integer avgDelay(Integer userId) {
        return conferenceAppointmentMapper.avgDelay(userId);
    }

    @Override
    public List<TodayConferenceVo> showTodayConference(Integer userId,String date) {
        List<TodayConferenceVo> todayConferenceVos = conferenceAppointmentMapper.showTodayConference(userId,date);
        for (TodayConferenceVo todayConferenceVo : todayConferenceVos) {
            //获取会议发起者姓名
            String name = userInfoMapper.selectByPrimaryKey(todayConferenceVo.getConferenceInitiator()).getName();
            todayConferenceVo.setInitatorName(name);
            //会议室名字
            Room room = roomMapper.selectByPrimaryKey(todayConferenceVo.getRoomId());
            todayConferenceVo.setRoomName(room.getName());
            //会议室地址
            todayConferenceVo.setAddress(room.getAddress());
        }
        return todayConferenceVos;
    }

    @Override
    public List<AllConferenceVo> showAllConferenceByUserId(Integer userId) {
        List<AllConferenceVo> allConferenceVoList=conferenceAppointmentMapper.showAllConferenceByUserId(userId);
        return allConferenceVoList;
    }
}
