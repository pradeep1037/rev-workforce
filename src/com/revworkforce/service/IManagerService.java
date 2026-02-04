package com.revworkforce.service;

import java.util.List;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveApplication;
import com.revworkforce.model.PerformanceReview;

public interface IManagerService {

    List<Employee> getMyTeam(int managerId);

    List<LeaveApplication> getPendingLeaveRequests(int managerId);

    boolean approveLeave(int leaveId, int managerId, String comment);

    boolean rejectLeave(int leaveId, int managerId, String comment);

    List<PerformanceReview> getTeamReviews(int managerId);

    boolean providePerformanceFeedback(int reviewId, int rating, String feedback);
}
