package com.revworkforce.service.impl;

import com.revworkforce.dao.*;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveApplication;
import com.revworkforce.model.Notifications;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.service.IManagerService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ManagerServiceImpl implements IManagerService {

    private final IEmployeeDao employeeDao;
    private final ILeaveDao leaveDao;
    private final ILeaveBalanceDao leaveBalanceDao;
    private final INotificationsDao notificationsDao;
    private final IPerformanceReviewDao performanceDao;

    public ManagerServiceImpl(
            IEmployeeDao employeeDao,
            ILeaveDao leaveDao,
            ILeaveBalanceDao leaveBalanceDao,
            INotificationsDao notificationsDao,
            IPerformanceReviewDao performanceDao) {

        this.employeeDao = employeeDao;
        this.leaveDao = leaveDao;
        this.leaveBalanceDao = leaveBalanceDao;
        this.notificationsDao = notificationsDao;
        this.performanceDao = performanceDao;
    }

    @Override
    public List<Employee> getMyTeam(int managerId) {
        return employeeDao.getEmployeesByManager(managerId);
    }

    @Override
    public List<LeaveApplication> getPendingLeaveRequests(int managerId) {
        return leaveDao.getPendingLeavesByManager(managerId);
    }

    @Override
    public boolean approveLeave(int leaveId, int managerId, String comment) {

        LeaveApplication leave = leaveDao.getLeaveById(leaveId);

        if (leave == null) {
            throw new RuntimeException("Leave request not found");
        }

        if (!"PENDING".equals(leave.getStatus())) {
            throw new RuntimeException("Leave already processed");
        }

        Employee emp = employeeDao.getEmployeeById(leave.getEmpId());

        if (emp == null || emp.getManagerId() != managerId) {
            throw new RuntimeException("Unauthorized approval");
        }

        // 1️⃣ Update leave status
        leaveDao.approveLeave(leaveId, managerId, comment);

        // 2️⃣ Deduct leave balance (inclusive days)
        long days = ChronoUnit.DAYS.between(
                leave.getStartDate(),
                leave.getEndDate()
        ) + 1;

        leaveBalanceDao.updateBalance(
                leave.getEmpId(),
                leave.getLeaveTypeId(),
                -(int) days
        );

        // 3️⃣ Notify employee WITH reason
        Notifications notification = new Notifications();
        notification.setUserId(emp.getUserId());
        notification.setType("LEAVE_APPROVED");
        notification.setMessage(
                "✅ Your leave has been approved.\n" +
                        "📝 Manager Comment: " + comment
        );
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationsDao.addNotification(notification);

        return true;
    }

    @Override
    public boolean rejectLeave(int leaveId, int managerId, String comment) {

        LeaveApplication leave = leaveDao.getLeaveById(leaveId);

        if (leave == null) {
            throw new RuntimeException("Leave request not found");
        }

        if (!"PENDING".equals(leave.getStatus())) {
            throw new RuntimeException("Leave already processed");
        }

        Employee emp = employeeDao.getEmployeeById(leave.getEmpId());

        if (emp == null || emp.getManagerId() != managerId) {
            throw new RuntimeException("Unauthorized rejection");
        }

        // 1️⃣ Update leave status
        leaveDao.rejectLeave(leaveId, managerId, comment);

        // 2️⃣ Notify employee WITH rejection reason
        Notifications notification = new Notifications();
        notification.setUserId(emp.getUserId());
        notification.setType("LEAVE_REJECTED");
        notification.setMessage(
                "❌ Your leave has been rejected.\n" +
                        "📝 Manager Comment: " + comment
        );
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationsDao.addNotification(notification);

        return true;
    }

    @Override
    public List<PerformanceReview> getTeamReviews(int managerId) {
        return performanceDao.getReviewsByManager(managerId);
    }

    @Override
    public boolean providePerformanceFeedback(int reviewId, int rating, String feedback) {
        return performanceDao.updateManagerFeedback(reviewId, rating, feedback);
    }
}
