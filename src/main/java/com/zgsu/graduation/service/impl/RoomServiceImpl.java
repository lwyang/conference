package com.zgsu.graduation.service.impl;

import com.zgsu.graduation.Vo.RoomVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.RoomMapper;
import com.zgsu.graduation.model.Room;
import com.zgsu.graduation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/10 20:09
 */
@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomMapper roomMapper;


    @Override
    public ErrorEnum addRoom(Room room, MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File("C:\\room_picture\\"+room.getName()+".jpg")));
            out.write(file.getBytes());
            out.flush();
            out.close();
            String picture="C:\\room_picture\\"+room.getName()+".jpg";
            room.setPicture(picture);
        }
        return roomMapper.insert(room)==1?ErrorEnum.SUCCESS:ErrorEnum.FAILURE;
    }

    @Override
    public List<RoomVo> showRoomList() {
        List<RoomVo> roomVoList=roomMapper.showRoomList();
        for(RoomVo roomVo:roomVoList){
            roomVo.setPicture(roomVo.getPicture().replace("C:\\room_picture","http:\\120.26.48.169:8081"));
        }
        return roomVoList;
    }

    @Override
    public ErrorEnum updateRoom(Room room) {
        return null;
    }

    @Override
    public ErrorEnum deleteRoom(Room room) {
        return null;
    }

    @Override
    public Room showRoomById(Integer id) {
        Room room=roomMapper.selectByPrimaryKey(id);
        room.setPicture(room.getPicture().replace("C:\\room_picture","http:\\120.26.48.169:8081"));
        return room;
    }
}
