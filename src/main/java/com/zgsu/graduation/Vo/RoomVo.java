package com.zgsu.graduation.Vo;

import lombok.Data;

@Data
public class RoomVo {
    private Integer id;
    /**
     * 会议室名称
     */
    private String name;
    /**
     * 会议室容量
     */
    private Integer capacity;
    /**
     * 会议室地址
     */
    private String address;
    /**
     * 会议室图片地址
     */
    private String picture;
    /**
     * 会议室状态，true代表可使用
     */
    private Boolean state;

    private Boolean audio;

    private Boolean mike;

    private Boolean projector;
}
