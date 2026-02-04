package com.revworkforce.service.impl;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;
import com.revworkforce.service.IEmployeeService;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class EmployeeServiceImpl implements IEmployeeService {

    private IEmployeeDao employeeDao;
    private ILeaveDao leaveDao;
    private ILeaveBalanceDao leaveBalanceDao;
    private INotificationsDao notificationsDao;
    private IPerformanceReviewDao performanceDao;
    private IGoalsDao iGoalsDao;

    public EmployeeServiceImpl(
            IEmployeeDao employeeDao,
            ILeaveDao leaveDao,
            ILeaveBalanceDao leaveBalanceDao,
            INotificationsDao notificationsDao,
            IPerformanceReviewDao performanceDao,
            IGoalsDao iGoalsDao) {

        this.employeeDao = employeeDao;
        this.leaveDao = leaveDao;
        this.leaveBalanceDao = leaveBalanceDao;
        this.notificationsDao = notificationsDao;
        this.performanceDao = performanceDao;
        this.iGoalsDao=iGoalsDao;
    }

    @Override
    public Employee getMyProfile(int userId) {
        return employeeDao.getEmployeeByUserId(userId);
    }

    @Override
    public Employee getMyManager(int empId) {
        Employee emp = employeeDao.getEmployeeById(empId);
        return employeeDao.getEmployeeById(emp.getManagerId());
    }

    @Override
    public boolean applyLeave(LeaveApplication leave) {

        if (leave.getEndDate().isBefore(leave.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        LeaveBalance balance = leaveBalanceDao.getBalance(
                leave.getEmpId(), leave.getLeaveTypeId());

        long days = ChronoUnit.DAYS.between(
                leave.getStartDate(), leave.getEndDate()) + 1;

        if (balance.getBalanceDays() < days) {
            throw new RuntimeException("Insufficient leave balance");
        }

        leave.setStatus("PENDING");
        leaveDao.applyLeave(leave);

        Employee emp = employeeDao.getEmployeeById(leave.getEmpId());
        Employee manager = employeeDao.getEmployeeById(emp.getManagerId());

        Notifications notifications = new Notifications();
        notifications.setUserId(manager.getUserId());
        notifications.setMessage("New leave request from " + emp.getFirstName());

        notificationsDao.addNotification(notifications);
        return true;
    }

    @Override
    public List<LeaveApplication> getMyLeaves(int empId) {
        return leaveDao.getLeavesByEmployee(empId);
    }

    @Override
    public List<Notifications> getMyNotifications(int userId) {
        return notificationsDao.getNotificationsByUserId(userId);
    }

    @Override
    public boolean markNotificationAsRead(int notificationId) {
        return notificationsDao.markAsRead(notificationId);
    }

    @Override
    public boolean submitPerformanceReview(PerformanceReview review) {
        return performanceDao.addPerformanceReview(review);
    }

    @Override
    public List<Goals> getMyGoals(int empId) {
        return iGoalsDao.getGoalsByEmpId(empId);
    }
}

