package com.zgsu.graduation.Vo;

import lombok.Data;

import java.sql.Time;

/**
 * 某用户所有会议列表实体类VO
 */
@Data
public class AllConferenceVo {
    //会议id
    private Integer id;
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
