package com.revworkforce.service;

import com.revworkforce.model.*;

import java.util.List;

public interface IAdminService {

    public boolean addEmployee(Users user, Employee employee);

    public boolean assignManager(int empId, int managerId);

    public boolean updateEmployeeStatus(int empId, boolean isActive);


    Employee getEmployeeProfile(int empId);

    boolean addDepartment(Department department);

    boolean updateDepartment(Department department);

    List<Department> getAllDepartments();

    boolean addHoliday(HolidayCalendar holiday);

    boolean removeHoliday(int holidayId);

    boolean createAnnouncement(Announcements announcement);

    boolean deleteAnnouncement(int announcementId);
}
