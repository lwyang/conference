package com.zgsu.graduation.mapper;

import com.zgsu.graduation.model.Department;
import com.zgsu.graduation.model.DepartmentExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

@Component
public interface DepartmentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    long countByExample(DepartmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    int deleteByExample(DepartmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    int deleteByPrimaryKey(Integer departmentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    int insert(Department record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    int insertSelective(Department record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    List<Department> selectByExampleWithRowbounds(DepartmentExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    List<Department> selectByExample(DepartmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    Department selectByPrimaryKey(Integer departmentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    int updateByExampleSelective(@Param("record") Department record, @Param("example") DepartmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    int updateByExample(@Param("record") Department record, @Param("example") DepartmentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    int updateByPrimaryKeySelective(Department record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table department
     *
     * @mbg.generated Fri Jan 03 17:04:36 CST 2020
     */
    int updateByPrimaryKey(Department record);

    @Select("select department_name from department where department_id=#{id}")
    String showDepartmentNameById(@Param("id") Integer id);
}