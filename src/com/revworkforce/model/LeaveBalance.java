package com.revworkforce.model;

public class LeaveBalance {

    private int balanceId;
    private int empId;
    private int leaveTypeId;
    private int balanceDays;

    public LeaveBalance() {}

    public LeaveBalance(int empId, int leaveTypeId, int balanceDays) {
        this.empId = empId;
        this.leaveTypeId = leaveTypeId;
        this.balanceDays = balanceDays;
    }

    public LeaveBalance(int balanceId, int empId, int leaveTypeId, int balanceDays) {
        this.balanceId = balanceId;
        this.empId = empId;
        this.leaveTypeId = leaveTypeId;
        this.balanceDays = balanceDays;
    }

    public int getBalanceId() { return balanceId; }
    public void setBalanceId(int balanceId) { this.balanceId = balanceId; }

    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public int getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(int leaveTypeId) { this.leaveTypeId = leaveTypeId; }

    public int getBalanceDays() { return balanceDays; }
    public void setBalanceDays(int balanceDays) { this.balanceDays = balanceDays; }

    @Override
    public String toString() {
        return "LeaveBalance{" +
                "balanceId=" + balanceId +
                ", empId=" + empId +
                ", leaveTypeId=" + leaveTypeId +
                ", balanceDays=" + balanceDays +
                '}';
    }
}
