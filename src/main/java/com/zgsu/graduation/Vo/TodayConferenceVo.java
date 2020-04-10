package com.zgsu.graduation.Vo;

import lombok.Data;

import java.sql.Time;

/**
 * 某用户当日所有参与会议信息实体类
 */
@Data
public class TodayConferenceVo {
    //会议id
    private Integer id;
    //会议发起者id
    private Integer conferenceInitiator;
    //会议发起者姓名
    private String initatorName;
    //会议室id
    private Integer roomId;
    //会议室名字
    private String roomName;
    //地址
    private String address;
    //开会日期
    private String date;
    //会议开始时间
    private Time startTime;
    //会议主题
    private String theme;
    //会议内容
    private String introduce;
    //会议状态
    private Integer state;
}
