package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.Department;
import com.zgsu.graduation.service.DepartmentService;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @ApiOperation(value = "显示部门列表")
    @GetMapping("/department")
    public ResultVo showDepartmentLiset(){
        List<Department> departmentList=departmentService.showDepartmentList();
        if(departmentList.size()>0){
            return ResultMsgUtil.success(departmentList);
        }else{
            return ResultMsgUtil.error(ErrorEnum.FAILURE.getCode(),ErrorEnum.FAILURE.getMsg());
        }
    }
    @ApiOperation(value = "添加部门")
    @PostMapping("/department")
    public ResultVo addDepartment(@RequestBody Department department){
        ErrorEnum errorEnum=departmentService.addDepartment(department);
        if(errorEnum.getCode().equals(200)){
            return ResultMsgUtil.success();
        }else{
            return ResultMsgUtil.error(ErrorEnum.ADD_FAILURE.getCode(),ErrorEnum.ADD_FAILURE.getMsg());
        }
    }
}
