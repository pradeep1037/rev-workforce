package com.revworkforce.dao;

import com.revworkforce.model.LeaveBalance;

public interface ILeaveBalanceDao {

    //  Get leave balance for employee + leave type
    LeaveBalance getBalance(int empId, int leaveTypeId);

    //  Update balance (positive or negative days)
    boolean updateBalance(int empId, int leaveTypeId, int days);

    //  Initialize leave balance when new employee is created
    boolean initializeBalance(int empId);
}
