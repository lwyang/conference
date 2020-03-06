package com.zgsu.graduation.Vo;

import lombok.Data;

import java.sql.Time;

/**
 * 会议室已占用时间段
 */
@Data
public class AppointmentTimeVo {
    /**
     * 会议开始时间
     */
    private Time startTime;
    /**
     * 会议结束时间
     */
    private Time endTime;
}
