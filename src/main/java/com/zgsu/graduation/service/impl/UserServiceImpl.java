package com.zgsu.graduation.service.impl;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;
import com.zgsu.graduation.Vo.UserInfoVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.DepartmentMapper;
import com.zgsu.graduation.mapper.UserInfoMapper;
import com.zgsu.graduation.model.Department;
import com.zgsu.graduation.model.DepartmentExample;
import com.zgsu.graduation.model.UserInfo;
import com.zgsu.graduation.model.UserInfoExample;
import com.zgsu.graduation.service.UserService;
import com.zgsu.graduation.utils.FaceEngineUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/3 18:22
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ErrorEnum addUser(UserInfo userInfo, MultipartFile file) {
        FaceEngine faceEngine = FaceEngineUtil.initEngine();
        //人脸检测
        //ImageInfo imageInfo = getRGBData(new File("d:\\1.jpg"));
        System.out.println(file.getOriginalFilename());
        ImageInfo imageInfo = null;
        try {
            imageInfo = getRGBData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        int detectCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        //System.out.println(faceInfoList);

        //特征提取
        FaceFeature faceFeature = new FaceFeature();
        //赋值前端传过来的人脸特征码用于和人脸特征数据库对比
        //faceFeature.setFeatureData(userInfo.getFaceFeature());
        int extractCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
        userInfo.setFaceFeature(faceFeature.getFeatureData());
        //从人脸特征数据库中取出所有人脸特征数据进行对比
        List<UserInfo> userInfoList = userInfoMapper.selectByExampleWithBLOBs(new UserInfoExample());
        if (userInfoList.size() > 0) {
            for (UserInfo userInfo1 : userInfoList) {
                FaceSimilar faceSimilar = new FaceSimilar();
                Float source = FaceEngineUtil.faceCompany(faceEngine, userInfo.getFaceFeature(), userInfo1.getFaceFeature());
                System.out.println(source);
                if (source >= 0.8) {
                    System.out.println("该人脸已注册，无法重复注册");
                    return ErrorEnum.REPEAT;
                }
            }
        }
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.format(now);
        userInfo.setCreatTime(now);
        userInfo.setUpdateTime(now);
        return userInfoMapper.insertSelective(userInfo) == 1 ? ErrorEnum.SUCCESS : ErrorEnum.FAILURE;

    }

    @Override
    public ErrorEnum updateUser(UserInfoVo userInfoVo) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoVo, userInfo);
        DepartmentExample departmentExample = new DepartmentExample();
        departmentExample.createCriteria().andDepartmentNameEqualTo(userInfoVo.getDepartment());
        List<Department> departments = departmentMapper.selectByExample(departmentExample);
        if (departments.size() > 0) {
            userInfo.setDepartmentId(departments.get(0).getDepartmentId());
        } else {
            return ErrorEnum.NO_DEPARTMENT;
        }
        userInfo.setGender(userInfoVo.getGender().equals("男") ? false : true);

        int result = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        return result == 1 ? ErrorEnum.SUCCESS : ErrorEnum.UPDATE_FAILURE;
    }

    @Override
    public ErrorEnum deleteUser(Integer id) {
        int result = userInfoMapper.deleteByPrimaryKey(id);
        return result == 1 ? ErrorEnum.SUCCESS : ErrorEnum.DELETE_FAILURE;
    }

    @Override
    public UserInfoVo showUser(Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        String departmentName = "暂无部门信息";
        if (userInfo.getDepartmentId() != null) {
            departmentName = departmentMapper.showDepartmentNameById(userInfo.getDepartmentId());
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        userInfoVo.setDepartment(departmentName);
        return userInfoVo;
    }

    @Override
    public List<Map<String, Object>> showAllUsers() {
        return userInfoMapper.showAllUsers();
    }

    @Override
    public int selectLastId() {
        return userInfoMapper.selectLastId();
    }

    @Override
    public List<Map<String, Object>> showUserByDepartmentId(Integer id) {
        return userInfoMapper.showUserByDepartmentId(id);
    }

    @Override
    public ErrorEnum updateUserDepartment(Integer userId, Integer departmentId) {
        int result = userInfoMapper.updateDepartment(userId, departmentId);
        return result == 1 ? ErrorEnum.SUCCESS : ErrorEnum.FAILURE;
    }

    @Override
    public List<Map<String, Object>> showFaceInfo() {
        return userInfoMapper.showFaceInfo();
    }
}
