package com.revworkforce.model;

import java.time.LocalDate;

public class Goals {

    private int goalId;
    private int empId;
    private String goalDesc;
    private LocalDate deadline;
    private String priority;
    private String successMetric;
    private int progress;
    private String status;

    public Goals() {}

    public Goals(int empId, String goalDesc, LocalDate deadline,
                 String priority, String successMetric, String status) {
        this.empId = empId;
        this.goalDesc = goalDesc;
        this.deadline = deadline;
        this.priority = priority;
        this.successMetric = successMetric;
        this.status = status;
    }

    public Goals(int goalId, int empId, String goalDesc, LocalDate deadline,
                 String priority, String successMetric, int progress, String status) {
        this.goalId = goalId;
        this.empId = empId;
        this.goalDesc = goalDesc;
        this.deadline = deadline;
        this.priority = priority;
        this.successMetric = successMetric;
        this.progress = progress;
        this.status = status;
    }

    public int getGoalId() { return goalId; }
    public void setGoalId(int goalId) { this.goalId = goalId; }

    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public String getGoalDesc() { return goalDesc; }
    public void setGoalDesc(String goalDesc) { this.goalDesc = goalDesc; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getSuccessMetric() { return successMetric; }
    public void setSuccessMetric(String successMetric) { this.successMetric = successMetric; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Goals{" +
                "goalId=" + goalId +
                ", empId=" + empId +
                ", progress=" + progress +
                ", status='" + status + '\'' +
                '}';
    }
}
