package com.zgsu.graduation.Vo;

import lombok.Data;

import java.util.List;

/**
 * 会议室推荐结果前端实体类
 */
@Data
public class RecommendVo {
    private  RoomVo roomVo;

    private List<AppointmentTimeVo> appointmentTimeVo;

}
