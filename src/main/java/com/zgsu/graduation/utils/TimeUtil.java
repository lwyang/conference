package com.zgsu.graduation.utils;

import com.zgsu.graduation.Vo.AppointmentTimeVo;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeUtil {
    /**
     * 计算某个会议室最大连续空闲时间
     *
     * @param list
     * @return
     */
    public static Long maxSpan(List<AppointmentTimeVo> list,String date) throws ParseException {
        //初始化最大空闲时间为0
        long max=0;
        Time now = new Time(System.currentTimeMillis());
        //对当前系统时间分钟取整0，15，30，45
        now=adjustTime(now);
        System.out.println(now);
        List<Time> startList = new ArrayList<>();
        List<Time> endList = new ArrayList<>();
        endList.add(now);
        for (AppointmentTimeVo appointmentTimeVo : list) {
            startList.add(appointmentTimeVo.getStartTime());
            endList.add(appointmentTimeVo.getEndTime());
        }
        //将晚上21点转化为Time类型，用于计算空闲时间
        String endTime=date+" 21:00:00";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startList.add(new Time(simpleDateFormat.parse(endTime).getTime()));
        for (int i = 0; i < startList.size(); i++) {
            System.out.println(startList.get(i).getTime());
            System.out.println(endList.get(i).getTime());
            Long span = (startList.get(i).getTime()-endList.get(i).getTime()) / 1000 / 60;
            if(span>max){
                max=span;
            }
            System.out.println(span);
        }
        return max;
    }

    /**
     * 对预约者发起推荐的系统时间调整至与会议室15分钟作为一个区间形式相同
     *
     * @return
     */
    public static Time adjustTime(Time time) {
        time.setSeconds(0);
        int min = time.getMinutes();
        if(min>0&&min<15)
            time.setMinutes(15);
        else if(min>15&&min<30)
            time.setMinutes(30);
        else if(min>30&&min<45)
            time.setTime(45);
        else if(min>45&&min<=59){
            time.setMinutes(0);
            time.setHours(time.getHours()+1);
        }
        return time;
    }

    /**
     * 根据用户延迟行为设置会议结束时间
     * @return
     */
    public static Time delayTime(Time time,Integer delay){
        if(delay<=15&&delay>0){
            delay=15;
        }else if(delay>15&&delay<=30){
            delay=30;
        }else if(delay>30&&delay<45){
            delay=45;
        }
        time.setTime(time.getTime()+ delay*60*1000);
        return time;
    }
}
