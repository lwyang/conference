package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.AppointmentTimeVo;
import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.Vo.RoomVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.Room;
import com.zgsu.graduation.service.ConferenceAppointmentService;
import com.zgsu.graduation.service.RoomService;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/15 13:22
 */
@RestController
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private ConferenceAppointmentService conferenceAppointmentService;

    @ApiOperation(value = "添加会议室")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "会议室状态，true代表可用，false代表不可用", required = true, paramType = "query"),
            @ApiImplicitParam(name = "serialNumber", value = "绑定设备序列号", required = true, paramType = "query")
    })
    @PostMapping("/room")
    public ResultVo addRoom(Room room, @RequestParam("file") MultipartFile file) throws IOException {
        ErrorEnum errorEnum = roomService.addRoom(room, file);
        if (errorEnum.getCode().equals(200)) {
            return ResultMsgUtil.success();
        } else {
            return ResultMsgUtil.error(errorEnum.getCode(), errorEnum.getMsg());
        }
    }

    @ApiOperation(value = "获取会议室列表")
    @GetMapping("/room")
    public ResultVo showRoomList() {
        List<RoomVo> roomVoList = roomService.showRoomList();
        return ResultMsgUtil.success(roomVoList);
    }

    @ApiOperation(value = "根据id获取会议室详细信息")
    @GetMapping("/room/id")
    public ResultVo getRoomById(@RequestParam("id") Integer id, @RequestParam("date") String date) {
        Room room = roomService.showRoomById(id);
        List<AppointmentTimeVo> appointmentTimeVos = conferenceAppointmentService.showAppointmentTime(id, date);
        Map<String, Object> map = new HashMap<>();
        map.put("room", room);
        map.put("time", appointmentTimeVos);
        return ResultMsgUtil.success(map);
    }

    @ApiOperation(value = "会议室绑定会议室前端设备")
    @PostMapping("/room/serialNumber")
    public ResultVo bindSerialNumber(@RequestParam("serialNumber") String serialNumber, @RequestParam("roomId") Integer roomId) {
        ErrorEnum errorEnum=roomService.bindSerialNumber(roomId, serialNumber);
        if(errorEnum.getCode()==200){
            return ResultMsgUtil.success("设备绑定成功");
        }
        return ResultMsgUtil.error(errorEnum.getCode(),errorEnum.getMsg());
    }
//    @InitBinder
//    public void initBinder(WebDataBinder binder, WebRequest request){
//        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
//    }

}
