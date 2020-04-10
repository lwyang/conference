package com.zgsu.graduation.service.impl;

import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.ConferenceAppointmentMapper;
import com.zgsu.graduation.model.ConferenceAppointment;
import com.zgsu.graduation.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FaceServiceImpl implements FaceService {
    @Autowired
    private ConferenceAppointmentMapper conferenceAppointmentMapper;

    @Override
    public ErrorEnum signOff(Integer userId, Integer conferenceId) {
        ConferenceAppointment ca=conferenceAppointmentMapper.selectByPrimaryKey(conferenceId);
        SimpleDateFormat sd=new SimpleDateFormat("HH:mm:ss");
        String et=sd.format(ca.getEndTime());
        Date endTime=null;
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            endTime=simpleDateFormat.parse(ca.getDate()+" "+et);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Time realEndTime = new Time(System.currentTimeMillis());
        Long diff=realEndTime.getTime()-endTime.getTime();
        Long minutes=(diff)/(1000*60);
        Integer delay=minutes.intValue();
        ca.setRealEndTime(realEndTime);
        ca.setDelay(delay);
        int result=conferenceAppointmentMapper.updateByPrimaryKey(ca);
        if(result==1){
            return ErrorEnum.SUCCESS;
        }
        return ErrorEnum.FAILURE;
    }


}
