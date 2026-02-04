package com.revworkforce.dao;

import com.revworkforce.model.PerformanceReview;
import java.util.List;

public interface IPerformanceReviewDao {

    boolean addPerformanceReview(PerformanceReview review);

    boolean updateManagerReview(PerformanceReview review);

    PerformanceReview getReviewById(int reviewId);

    List<PerformanceReview> getReviewsByEmpId(int empId);

    boolean deleteReview(int reviewId);

    boolean updateManagerFeedback(int reviewId, int rating, String feedback);

    List<PerformanceReview> getReviewsByManager(int managerId);
}
