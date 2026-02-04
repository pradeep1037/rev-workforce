package com.revworkforce.dao;

import com.revworkforce.model.Department;
import java.util.List;

public interface IDepartmentDao {

    boolean addDepartment(Department department);

    boolean updateDepartment(Department department);

    Department getDepartmentById(int departmentId);

    List<Department> getAllDepartments();

    boolean deleteDepartment(int departmentId);

    boolean existsByName(String departmentName);

}
