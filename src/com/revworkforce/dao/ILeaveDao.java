package com.revworkforce.dao;

import com.revworkforce.model.LeaveApplication;
import java.util.List;

public interface ILeaveDao {

    boolean applyLeave(LeaveApplication leave);

    boolean approveLeave(int leaveId, int managerId, String comment);


    boolean rejectLeave(int leaveId, int managerId, String comment);

    List<LeaveApplication> getLeavesByEmployee(int empId);

    List<LeaveApplication> getPendingLeavesByManager(int managerId);

    LeaveApplication getLeaveById(int leaveId);
}
