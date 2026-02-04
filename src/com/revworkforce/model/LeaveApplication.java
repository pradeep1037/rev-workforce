package com.revworkforce.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveApplication {

    private int leaveId;
    private int empId;
    private int leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
    private LocalDateTime appliedOn;
    private Integer approvedBy;
    private String managerComment;

    public LeaveApplication() {}

    public LeaveApplication(int empId, int leaveTypeId, LocalDate startDate,
                            LocalDate endDate, String reason, String status,
                            LocalDateTime appliedOn) {
        this.empId = empId;
        this.leaveTypeId = leaveTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.appliedOn = appliedOn;
    }

    public LeaveApplication(int leaveId, int empId, int leaveTypeId,
                            LocalDate startDate, LocalDate endDate, String reason,
                            String status, LocalDateTime appliedOn,
                            Integer approvedBy, String managerComment) {
        this.leaveId = leaveId;
        this.empId = empId;
        this.leaveTypeId = leaveTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.appliedOn = appliedOn;
        this.approvedBy = approvedBy;
        this.managerComment = managerComment;
    }

    public int getLeaveId() { return leaveId; }
    public void setLeaveId(int leaveId) { this.leaveId = leaveId; }

    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public int getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(int leaveTypeId) { this.leaveTypeId = leaveTypeId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getAppliedOn() { return appliedOn; }
    public void setAppliedOn(LocalDateTime appliedOn) { this.appliedOn = appliedOn; }

    public Integer getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Integer approvedBy) { this.approvedBy = approvedBy; }

    public String getManagerComment() { return managerComment; }
    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }

    @Override
    public String toString() {
        return "LeaveApplication{" +
                "leaveId=" + leaveId +
                ", empId=" + empId +
                ", leaveTypeId=" + leaveTypeId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", managerComment='" + managerComment + '\'' +
                ", approvedBy=" + approvedBy +
                ", appliedOn=" + appliedOn +
                '}';
    }

}

