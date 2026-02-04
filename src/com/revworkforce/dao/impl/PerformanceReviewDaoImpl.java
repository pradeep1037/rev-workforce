package com.revworkforce.dao.impl;

import com.revworkforce.dao.IPerformanceReviewDao;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceReviewDaoImpl implements IPerformanceReviewDao {

    // ================= SQL QUERIES =================

    private static final String INSERT = """
        INSERT INTO performance_review
        (emp_id, year, achievements, improvements, self_rating, status)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE_MANAGER_REVIEW = """
        UPDATE performance_review
        SET manager_rating = ?, manager_feedback = ?, status = ?
        WHERE review_id = ?
    """;

    private static final String UPDATE_MANAGER_FEEDBACK = """
        UPDATE performance_review
        SET manager_rating = ?, manager_feedback = ?
        WHERE review_id = ?
    """;

    private static final String SELECT_BY_ID =
            "SELECT * FROM performance_review WHERE review_id = ?";

    private static final String SELECT_BY_EMP =
            "SELECT * FROM performance_review WHERE emp_id = ?";

    private static final String SELECT_BY_MANAGER = """
        SELECT pr.*
        FROM performance_review pr
        JOIN employee e ON pr.emp_id = e.emp_id
        WHERE e.manager_id = ?
    """;

    private static final String DELETE =
            "DELETE FROM performance_review WHERE review_id = ?";

    // ================= DAO METHODS =================

    @Override
    public boolean addPerformanceReview(PerformanceReview review) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {

            ps.setInt(1, review.getEmpId());
            ps.setInt(2, review.getYear());
            ps.setString(3, review.getAchievements());
            ps.setString(4, review.getImprovements());
            ps.setInt(5, review.getSelfRating());
            ps.setString(6, review.getStatus());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("addPerformanceReview", e);
            return false;
        }
    }

    @Override
    public boolean updateManagerReview(PerformanceReview review) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_MANAGER_REVIEW)) {

            ps.setInt(1, review.getManagerRating());
            ps.setString(2, review.getManagerFeedback());
            ps.setString(3, review.getStatus());
            ps.setInt(4, review.getReviewId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updateManagerReview", e);
            return false;
        }
    }

    @Override
    public boolean updateManagerFeedback(int reviewId, int rating, String feedback) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_MANAGER_FEEDBACK)) {

            ps.setInt(1, rating);
            ps.setString(2, feedback);
            ps.setInt(3, reviewId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updateManagerFeedback", e);
            return false;
        }
    }

    @Override
    public PerformanceReview getReviewById(int reviewId) {
        return fetchSingle(SELECT_BY_ID, reviewId);
    }

    @Override
    public List<PerformanceReview> getReviewsByEmpId(int empId) {
        return fetchList(SELECT_BY_EMP, empId);
    }

    @Override
    public List<PerformanceReview> getReviewsByManager(int managerId) {
        return fetchList(SELECT_BY_MANAGER, managerId);
    }

    @Override
    public boolean deleteReview(int reviewId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, reviewId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("deleteReview", e);
            return false;
        }
    }

    // ================= FETCH HELPERS =================

    private PerformanceReview fetchSingle(String sql, int param) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, param);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }

        } catch (SQLException e) {
            logError("fetchSingle", e);
            return null;
        }
    }

    private List<PerformanceReview> fetchList(String sql, int param) {

        List<PerformanceReview> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, param);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            logError("fetchList", e);
        }

        return list;
    }

    // ================= ROW MAPPER =================

    private PerformanceReview mapRow(ResultSet rs) throws SQLException {

        PerformanceReview pr = new PerformanceReview();

        pr.setReviewId(rs.getInt("review_id"));
        pr.setEmpId(rs.getInt("emp_id"));
        pr.setYear(rs.getInt("year"));
        pr.setAchievements(rs.getString("achievements"));
        pr.setImprovements(rs.getString("improvements"));
        pr.setSelfRating(rs.getInt("self_rating"));
        pr.setManagerRating(rs.getInt("manager_rating"));
        pr.setManagerFeedback(rs.getString("manager_feedback"));
        pr.setStatus(rs.getString("status"));

        return pr;
    }

    // ================= LOGGING =================

    private void logError(String method, SQLException e) {
        System.err.println("[PerformanceReviewDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
