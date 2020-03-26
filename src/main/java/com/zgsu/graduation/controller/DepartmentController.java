package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.Department;
import com.zgsu.graduation.service.DepartmentService;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "显示部门列表")
    @GetMapping("/department")
    public ResultVo showDepartmentLiset() {
        List<Department> departmentList = departmentService.showDepartmentList();
        if (departmentList.size() > 0) {
            return ResultMsgUtil.success(departmentList);
        } else {
            return ResultMsgUtil.error(ErrorEnum.FAILURE.getCode(), ErrorEnum.FAILURE.getMsg());
        }
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/department")
    public ResultVo addDepartment(@RequestParam("departmentName") String departmentName) {
        ErrorEnum errorEnum = departmentService.addDepartment(departmentName);
        if (errorEnum.getCode().equals(200)) {
            return ResultMsgUtil.success();
        } else {
            return ResultMsgUtil.error(errorEnum.getCode(), errorEnum.getMsg());
        }
    }

    @ApiOperation(value = "修改部门，暂未具体实现")
    @PostMapping("/dep")
    public ResultVo updateDepartment() {
        return null;
    }
}
