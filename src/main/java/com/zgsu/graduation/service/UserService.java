package com.zgsu.graduation.service;

import com.zgsu.graduation.Vo.UserInfoVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/3 17:07
 */
@Service
public interface UserService {
    /**
     * 用户注册
     *
     * @param userInfo
     * @return
     */
    ErrorEnum addUser(UserInfo userInfo, MultipartFile file);

    /**
     * 更新用户信息
     *
     * @param userInfoVo
     * @return
     */
    ErrorEnum updateUser(UserInfoVo userInfoVo);

    /**
     * 根据id删除用户信息
     *
     * @param id
     * @return
     */
    ErrorEnum deleteUser(Integer id);

    /**
     * 查看用户信息
     *
     * @param id
     * @return
     */
    UserInfoVo showUser(Integer id);

    List<Map<String, Object>> showAllUsers();

    /**
     * 获取刚插入数据库用户id
     *
     * @return
     */
    int selectLastId();

    /**
     * 根据部门id获取用户列表
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> showUserByDepartmentId(Integer id);

    ErrorEnum updateUserDepartment(Integer userId, Integer departmentId);

    List<Map<String,Object>> showFaceInfo();

    UserInfo login(String account,String password);

    UserInfo findUserById(Integer userId);
}
