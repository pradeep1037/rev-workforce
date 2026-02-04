package com.revworkforce.model;

public class LeaveType {

    private int leaveTypeId;
    private String leaveName;
    private int maxPerYear;

    public LeaveType() {}

    public LeaveType(String leaveName, int maxPerYear) {
        this.leaveName = leaveName;
        this.maxPerYear = maxPerYear;
    }

    public LeaveType(int leaveTypeId, String leaveName, int maxPerYear) {
        this.leaveTypeId = leaveTypeId;
        this.leaveName = leaveName;
        this.maxPerYear = maxPerYear;
    }

    public int getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(int leaveTypeId) { this.leaveTypeId = leaveTypeId; }

    public String getLeaveName() { return leaveName; }
    public void setLeaveName(String leaveName) { this.leaveName = leaveName; }

    public int getMaxPerYear() { return maxPerYear; }
    public void setMaxPerYear(int maxPerYear) { this.maxPerYear = maxPerYear; }

    @Override
    public String toString() {
        return "LeaveType{" +
                "leaveTypeId=" + leaveTypeId +
                ", leaveName='" + leaveName + '\'' +
                ", maxPerYear=" + maxPerYear +
                '}';
    }
}
