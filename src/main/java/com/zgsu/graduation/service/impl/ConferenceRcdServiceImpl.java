package com.zgsu.graduation.service.impl;

import com.zgsu.graduation.Vo.AppointmentTimeVo;
import com.zgsu.graduation.Vo.RoomVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.AppointmentConditionsMapper;
import com.zgsu.graduation.mapper.ConferenceAppointmentMapper;
import com.zgsu.graduation.mapper.ConferenceParticipantMapper;
import com.zgsu.graduation.mapper.RoomMapper;
import com.zgsu.graduation.model.*;
import com.zgsu.graduation.service.ConferenceRcdService;
import com.zgsu.graduation.utils.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConferenceRcdServiceImpl implements ConferenceRcdService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private ConferenceAppointmentMapper conferenceAppointmentMapper;
    @Autowired
    private AppointmentConditionsMapper appointmentConditionsMapper;
    @Autowired
    private ConferenceParticipantMapper conferenceParticipantMapper;

    @Override
    public List<RoomVo> conferenceCommend(AppointmentConditions appointmentConditions) throws ParseException {
        //根据设备以及参会者人数筛选合适的会议室
        RoomExample roomExample = new RoomExample();
        roomExample.createCriteria().andCapacityGreaterThanOrEqualTo(appointmentConditions.getNumbers())
                .andAudioGreaterThanOrEqualTo(appointmentConditions.getIsAudio())
                .andMikeGreaterThanOrEqualTo(appointmentConditions.getIsMike())
                .andProjectorGreaterThanOrEqualTo(appointmentConditions.getIsProjector())
                .andStateEqualTo(true);
        List<Room> roomList = roomMapper.selectByExample(roomExample);
        if (appointmentConditions.getDuration() != null) {
            //获取系统时间，查询会议结束时间比当前时间晚的会议室已预约时间段
            Time now = new Time(System.currentTimeMillis());
            //依次判断每个会议室某天连续空闲时间是否满足预约者要求
            for (int i = roomList.size() - 1; i >= 0; i--) {
                List<AppointmentTimeVo> appointmentTimeVoList = conferenceAppointmentMapper.showAppointMentTime(roomList.get(i).getId(), appointmentConditions.getDate(), now);
                Long max = TimeUtil.maxSpan(appointmentTimeVoList, appointmentConditions.getDate());
                //最大空闲时间不满足预约者要求，将该会议室移除
                if (max < appointmentConditions.getDuration())
                    roomList.remove(i);
            }
        } else {
            //先看看加上延迟a时间的情况下是否有满足的会议室
            for (int i = roomList.size() - 1; i >= 0; i--) {
                //依次判断每个会议室是否在时间上有冲突，有则移除该会议室推荐
                List<ConferenceAppointment> conferenceAppointmentList = conferenceAppointmentMapper.showAppointment(roomList.get(i).getId(), appointmentConditions.getDate(),
                        new Time(appointmentConditions.getStartTime().getTime()),
                        TimeUtil.delayTime(new Time(appointmentConditions.getEndTime().getTime()),conferenceAppointmentMapper.avgDelay(1)));
                if (conferenceAppointmentList.size() > 0) {
                    roomList.remove(i);
                }
            }
            //如果加上延迟a时间的情况下没有满足的会议室，则看看取消延迟时间的情况下是否有会议室
            if(roomList.size()==0){
                for (int i = roomList.size() - 1; i >= 0; i--) {
                    //依次判断每个会议室是否在时间上有冲突，有则移除该会议室推荐
                    List<ConferenceAppointment> conferenceAppointmentList = conferenceAppointmentMapper.showAppointment(roomList.get(i).getId(), appointmentConditions.getDate(),
                            new Time(appointmentConditions.getStartTime().getTime()),
                           new Time(appointmentConditions.getEndTime().getTime()));
                    if (conferenceAppointmentList.size() > 0) {
                        roomList.remove(i);
                    }
                }
            }
        }
        List<RoomVo> roomVoList = new ArrayList<>();
        if (roomList.size() > 0) {
            for (Room room : roomList) {
                RoomVo roomVo = new RoomVo();
                roomVo.setId(room.getId());
                roomVo.setAddress(room.getAddress());
                roomVo.setName(room.getName());
                //BeanUtils.copyProperties(room, roomVo);
                roomVoList.add(roomVo);
            }
        }
        return roomVoList;
    }

    @Override
    public ErrorEnum addRcdConditions(AppointmentConditions appointmentConditions) {
        int result = appointmentConditionsMapper.insertSelective(appointmentConditions);
        return result == 1 ? ErrorEnum.SUCCESS : ErrorEnum.FAILURE;
    }

    @Override
    public ErrorEnum evaluateRoom(Integer conferenceId, Integer participantId, Float mark, String evaluate) {
        if (mark < 0 || mark > 10) {
            return ErrorEnum.ERROR_MARK;
        }
        //根据会议id获取roomId
       ConferenceAppointment conferenceAppointment= conferenceAppointmentMapper.selectByPrimaryKey(conferenceId);
        Integer roomId=conferenceAppointment.getRoomId();
        //将参会者的评分记录到数据库
        ConferenceParticipantExample conferenceParticipantExample = new ConferenceParticipantExample();
        conferenceParticipantExample.createCriteria().andConferenceIdEqualTo(conferenceId).andParticipantEqualTo(participantId);
        ConferenceParticipant cp = new ConferenceParticipant();
        cp.setConferenceId(conferenceId);
        cp.setParticipant(participantId);
        cp.setMark(mark);
        cp.setEvaluate(evaluate);
        Integer cpResult=conferenceParticipantMapper.updateByExampleSelective(cp,conferenceParticipantExample);
        if(cpResult==0){
            return ErrorEnum.FAILURE;
        }

        Room room=roomMapper.selectByPrimaryKey(roomId);
        //获取该会议室的评分人数
        Integer judges=room.getJudges();
        Float score=room.getScore();
        score=(score*judges+mark)/(judges+1);
        judges=judges+1;
        room.setJudges(judges);
        room.setScore(score);
        //更新表
        Integer roomResult=roomMapper.updateByPrimaryKey(room);
        if(roomResult!=0){
            return ErrorEnum.SUCCESS;
        }
        //默认失败
        return ErrorEnum.FAILURE;
    }
}
