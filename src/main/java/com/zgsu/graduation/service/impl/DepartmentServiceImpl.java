package com.zgsu.graduation.service.impl;

import com.arcsoft.face.enums.ErrorInfo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.DepartmentMapper;
import com.zgsu.graduation.model.Department;
import com.zgsu.graduation.model.DepartmentExample;
import com.zgsu.graduation.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ErrorEnum addDepartment(String departmentName) {
        DepartmentExample departmentExample = new DepartmentExample();
        departmentExample.createCriteria().andDepartmentNameEqualTo(departmentName);
       List<Department> departmentList=departmentMapper.selectByExample(departmentExample);
       if(departmentList.size()>0){
           return ErrorEnum.EXIST_DEPARTMENT;
       }else{
           Department department=new Department();
           department.setDepartmentName(departmentName);
           return departmentMapper.insertSelective(department) == 1 ? ErrorEnum.SUCCESS : ErrorEnum.ADD_FAILURE;
       }

    }

    @Override
    public List<Department> showDepartmentList() {
        return departmentMapper.selectByExample(new DepartmentExample());
    }

    @Override
    public ErrorEnum deleteDepartmentById() {
        return null;
    }

    @Override
    public ErrorEnum updateDepartment() {
        return null;
    }
}
