package com.zgsu.graduation.service;

import com.zgsu.graduation.Vo.RoomVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/10 18:45
 */
public interface RoomService {
    /**
     * 添加会议室
     *
     * @param room
     * @return
     */
    ErrorEnum addRoom(Room room, MultipartFile file) throws IOException;

    List<RoomVo> showRoomList();

    ErrorEnum updateRoom(Room room);

    ErrorEnum deleteRoom(Room room);

    Room showRoomById(Integer id);
}
