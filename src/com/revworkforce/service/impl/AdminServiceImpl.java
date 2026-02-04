package com.revworkforce.service.impl;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;
import com.revworkforce.service.IAdminService;

import java.time.LocalDateTime;
import java.util.List;

public class AdminServiceImpl implements IAdminService {

    private IUserDao userDao;
    private IEmployeeDao employeeDao;
    private ILeaveBalanceDao leaveBalanceDao;
    private IDepartmentDao departmentDao;
    private IAnnouncementsDao announcementDao;
    private IHolidayCalendarDao holidayDao;
    private INotificationsDao notificationsDao;

    public AdminServiceImpl(
            IUserDao userDao,
            IEmployeeDao employeeDao,
            ILeaveBalanceDao leaveBalanceDao,
            IDepartmentDao departmentDao,
            IAnnouncementsDao announcementDao,
            IHolidayCalendarDao holidayDao,
            INotificationsDao notificationsDao) {

        this.userDao = userDao;
        this.employeeDao = employeeDao;
        this.leaveBalanceDao = leaveBalanceDao;
        this.departmentDao = departmentDao;
        this.announcementDao = announcementDao;
        this.holidayDao = holidayDao;
        this.notificationsDao=notificationsDao;
    }

//    @Override
//    public boolean addEmployee(Users user, Employee employee) {
//
//        if (userDao.getUserByEmail(user.getEmail()) != null) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        if (departmentDao.getDepartmentById(employee.getDepartmentId()) == null) {
//            throw new RuntimeException("Invalid department");
//        }
//
//        // 1. Create user
//        userDao.createUser(user);
//
//        // 2. Create employee
//        employee.setUserId(user.getUserId());
//        employeeDao.addEmployee(employee);
//
//        // 3. Initialize leave balance
//        leaveBalanceDao.initializeBalance(employee.getEmpId());
//
//        return true;
//    }

    @Override
    public boolean addEmployee(Users user, Employee employee) {

        // 1️⃣ CREATE USER
        int userId = userDao.createUser(user);
        if (userId <= 0) {
            throw new RuntimeException("User creation failed");
        }

        // 2️⃣ SET GENERATED user_id INTO EMPLOYEE
        employee.setUserId(userId);

        // 3️⃣ CREATE EMPLOYEE
        int empId = employeeDao.addEmployee(employee);
        if (empId <= 0) {
            throw new RuntimeException("Employee creation failed");
        }

        // 4️⃣ INITIALIZE LEAVE BALANCE
        boolean balanceInit = leaveBalanceDao.initializeBalance(empId);
        if (!balanceInit) {
            throw new RuntimeException("Leave balance initialization failed");
        }

        return true;
    }


    @Override
    public boolean assignManager(int empId, int managerId) {

        if (empId == managerId) {
            throw new RuntimeException("Employee cannot be their own manager");
        }

        if (employeeDao.getEmployeeById(managerId) == null) {
            throw new RuntimeException("Invalid manager");
        }

        return employeeDao.updateManager(empId, managerId);
    }

    @Override
    public boolean updateEmployeeStatus(int empId, boolean isActive) {
        return employeeDao.updateEmployeeStatus(empId, isActive);
    }

    @Override
    public Employee getEmployeeProfile(int empId) {
        return employeeDao.getEmployeeById(empId);
    }

    @Override
    public boolean addDepartment(Department department) {
        if (departmentDao.existsByName(department.getDepartmentName())) {
            throw new RuntimeException("Department already exists");
        }
        return departmentDao.addDepartment(department);
    }

    @Override
    public boolean updateDepartment(Department department) {
        return departmentDao.updateDepartment(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentDao.getAllDepartments();
    }

    @Override
    public boolean addHoliday(HolidayCalendar holiday) {
        return holidayDao.addHoliday(holiday);
    }

    @Override
    public boolean removeHoliday(int holidayId) {
        return holidayDao.deleteHoliday(holidayId);
    }

    @Override
    public boolean createAnnouncement(Announcements announcement) {

        // 1️⃣ Save announcement
        boolean saved = announcementDao.createAnnouncement(announcement);
        if (!saved) return false;

        // 2️⃣ Fetch all active users
        List<Users> users = userDao.getAllActiveUsers();

        // 3️⃣ Create notification for each user
        for (Users user : users) {

            Notifications notif = new Notifications();
            notif.setUserId(user.getUserId());
            notif.setMessage("📢 Announcement: " + announcement.getTitle());
            notif.setType("ANNOUNCEMENT");
            notif.setRead(false);
            notif.setCreatedAt(LocalDateTime.now());

            notificationsDao.addNotification(notif);
        }

        return true;
    }


    @Override
    public boolean deleteAnnouncement(int announcementId) {
        return announcementDao.deleteAnnouncement(announcementId);
    }
}

