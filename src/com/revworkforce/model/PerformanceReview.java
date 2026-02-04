package com.revworkforce.model;

public class PerformanceReview {

    private int reviewId;
    private int empId;
    private int year;
    private String achievements;
    private String improvements;
    private int selfRating;
    private int managerRating;
    private String managerFeedback;
    private String status;

    public PerformanceReview() {}

    public PerformanceReview(int empId, int year, String achievements,
                             String improvements, int selfRating, String status) {
        this.empId = empId;
        this.year = year;
        this.achievements = achievements;
        this.improvements = improvements;
        this.selfRating = selfRating;
        this.status = status;
    }

    public PerformanceReview(int reviewId, int empId, int year,
                             String achievements, String improvements,
                             int selfRating, int managerRating,
                             String managerFeedback, String status) {
        this.reviewId = reviewId;
        this.empId = empId;
        this.year = year;
        this.achievements = achievements;
        this.improvements = improvements;
        this.selfRating = selfRating;
        this.managerRating = managerRating;
        this.managerFeedback = managerFeedback;
        this.status = status;
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getAchievements() { return achievements; }
    public void setAchievements(String achievements) { this.achievements = achievements; }

    public String getImprovements() { return improvements; }
    public void setImprovements(String improvements) { this.improvements = improvements; }

    public int getSelfRating() { return selfRating; }
    public void setSelfRating(int selfRating) { this.selfRating = selfRating; }

    public int getManagerRating() { return managerRating; }
    public void setManagerRating(int managerRating) { this.managerRating = managerRating; }

    public String getManagerFeedback() { return managerFeedback; }
    public void setManagerFeedback(String managerFeedback) { this.managerFeedback = managerFeedback; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "PerformanceReview{" +
                "reviewId=" + reviewId +
                ", empId=" + empId +
                ", year=" + year +
                ", status='" + status + '\'' +
                '}';
    }
}
