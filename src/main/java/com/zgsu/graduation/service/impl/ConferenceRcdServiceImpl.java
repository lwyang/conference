package com.zgsu.graduation.service.impl;

import com.zgsu.graduation.Vo.AppointmentTimeVo;
import com.zgsu.graduation.Vo.RoomVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.AppointmentConditionsMapper;
import com.zgsu.graduation.mapper.ConferenceAppointmentMapper;
import com.zgsu.graduation.mapper.RoomMapper;
import com.zgsu.graduation.model.AppointmentConditions;
import com.zgsu.graduation.model.ConferenceAppointment;
import com.zgsu.graduation.model.Room;
import com.zgsu.graduation.model.RoomExample;
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
            //获取系统时间，查询会议结束时间比当前时间晚的已预约时间段
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
            for (int i = roomList.size() - 1; i >= 0; i--) {
                List<ConferenceAppointment>conferenceAppointmentList=conferenceAppointmentMapper.showAppointment(roomList.get(i).getId(),appointmentConditions.getDate(),
                        new Time(appointmentConditions.getStartTime().getTime()),
                        new Time(appointmentConditions.getEndTime().getTime()));
                if(conferenceAppointmentList.size()>0){
                    roomList.remove(i);
                }
            }
        }
        List<RoomVo> roomVoList = new ArrayList<>();
        if (roomList.size() > 0) {
            for (Room room : roomList) {
                RoomVo roomVo = new RoomVo();
                BeanUtils.copyProperties(room, roomVo);
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
}
