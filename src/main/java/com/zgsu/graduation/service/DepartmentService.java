package com.zgsu.graduation.service;

import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.model.Department;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DepartmentService {
    /**
     * 添加部门
     * @return
     */
    ErrorEnum addDepartment(Department department);

    /**
     * 显示部门列表
     * @return
     */
    List<Department> showDepartmentList();

    /**
     * 根据部门id删除
     * @return
     */
    ErrorEnum deleteDepartmentById();

    /**
     * 修改部门
     */
    ErrorEnum updateDepartment();
}
