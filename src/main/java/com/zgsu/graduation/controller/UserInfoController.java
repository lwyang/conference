package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.Vo.UserInfoVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.enums.ResultEnum;
import com.zgsu.graduation.model.UserInfo;
import com.zgsu.graduation.service.UserService;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.goeasy.GoEasy;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/7 10:02
 */
@RestController
public class UserInfoController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "添加用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户姓名", required = false, paramType = "query")
    })
    @PostMapping("/user")
    public ResultVo addUser(UserInfo userInfo, @RequestParam("file") MultipartFile file) {
        ErrorEnum errorEnum = userService.addUser(userInfo, file);
        if (errorEnum.getCode().equals(200)) {
            return ResultMsgUtil.success(userService.selectLastId());
        } else {
            return ResultMsgUtil.error(ResultEnum.FAILURE.getCode(), errorEnum.getMsg());
        }

    }

    @ApiOperation(value = "根据用户id获取用户信息")
    @GetMapping("/user")
    public ResultVo getUserById(@RequestParam("userId") Integer userId) {
        UserInfoVo userInfoVo = userService.showUser(userId);
        userInfoVo.setFaceFeature(null);
        return ResultMsgUtil.success(userInfoVo);
    }

    @ApiOperation(value = "获取所有用户信息")
    @GetMapping("/users")
    public ResultVo getUsers() {
        return ResultMsgUtil.success(userService.showAllUsers());

    }

    @ApiOperation(value = "更新用户信息")
    @PostMapping("/updateUser")
    public ResultVo updateUserInfo(@RequestBody UserInfoVo userInfoVo) {
        ErrorEnum errorEnum = userService.updateUser(userInfoVo);
        if (errorEnum.getCode().equals(200)) {
            return ResultMsgUtil.success();
        } else {
            return ResultMsgUtil.error(ResultEnum.FAILURE.getCode(), errorEnum.getMsg());
        }
    }

    @ApiOperation(value = "根据id删除用户")
    @DeleteMapping("/user")
    public ResultVo deleteUserInfo(@RequestParam("userId") Integer userId) {
        ErrorEnum errorEnum = userService.deleteUser(userId);
        if (errorEnum.getCode().equals(200)) {
            return ResultMsgUtil.success();
        } else {
            return ResultMsgUtil.error(ResultEnum.FAILURE.getCode(), errorEnum.getMsg());
        }

    }

    @ApiOperation(value = "获取部门用户列表")
    @GetMapping("/user/department")
    public ResultVo showUserByDepartmentId(@RequestParam("id") Integer id) {
        List<Map<String, Object>> userList = userService.showUserByDepartmentId(id);
        return ResultMsgUtil.success(userList);
    }

    @ApiOperation(value = "设置用户部门信息")
    @PostMapping("/user/department")
    public ResultVo updateUserDepartment(@RequestParam("userId") Integer userId, @RequestParam("departmentId") Integer departmentId) {
        ErrorEnum errorEnum = userService.updateUserDepartment(userId, departmentId);
        if (errorEnum.getCode().equals(200)) {
            return ResultMsgUtil.success();
        } else {
            return ResultMsgUtil.error(errorEnum.getCode(), errorEnum.getMsg());
        }
    }

    @GetMapping("/test")
    public String test(@RequestParam("name") String name) {
        throw new NullPointerException();
//        GoEasy goEasy=new GoEasy("http://rest-hangzhou.goeasy.io/publish","BC-fd94510bceb2485fa37fe7caaeb06b27");
//        goEasy.publish("test","hello");
       // return "hello" + name;

    }

}
