package com.revworkforce.service;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveApplication;
import com.revworkforce.model.Notifications;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.model.Goals;

import java.util.List;

public interface IEmployeeService {

    Employee getMyProfile(int userId);

    Employee getMyManager(int empId);

    boolean applyLeave(LeaveApplication leave);

    List<LeaveApplication> getMyLeaves(int empId);

    List<Notifications> getMyNotifications(int userId);

    boolean markNotificationAsRead(int notificationId);

    boolean submitPerformanceReview(PerformanceReview review);

    List<Goals> getMyGoals(int empId);
}
