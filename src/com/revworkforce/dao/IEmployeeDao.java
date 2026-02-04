package com.revworkforce.dao;

import com.revworkforce.model.Employee;
import java.util.List;

public interface IEmployeeDao {

    int addEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    Employee getEmployeeById(int empId);

    List<Employee> getEmployeesByDepartment(int departmentId);

    List<Employee> getEmployeesByManager(int managerId);

    boolean updateManager(int empId, int managerId);

    boolean updateEmployeeStatus(int empId, boolean isActive);

    Employee getEmployeeByUserId(int userId);

    List<Employee> getAllEmployees();
}
