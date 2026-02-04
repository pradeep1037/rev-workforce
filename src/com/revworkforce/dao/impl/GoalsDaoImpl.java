package com.revworkforce.dao.impl;

import com.revworkforce.dao.IGoalsDao;
import com.revworkforce.model.Goals;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoalsDaoImpl implements IGoalsDao {

    // ================= SQL QUERIES =================

    private static final String INSERT = """
        INSERT INTO goals
        (emp_id, goal_desc, deadline, priority, success_metric, status)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE_PROGRESS = """
        UPDATE goals
        SET progress = ?, status = ?
        WHERE goal_id = ?
    """;

    private static final String SELECT_BY_ID =
            "SELECT * FROM goals WHERE goal_id = ?";

    private static final String SELECT_BY_EMP =
            "SELECT * FROM goals WHERE emp_id = ?";

    private static final String DELETE =
            "DELETE FROM goals WHERE goal_id = ?";

    // ================= CRUD METHODS =================

    @Override
    public boolean addGoal(Goals goal) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {

            ps.setInt(1, goal.getEmpId());
            ps.setString(2, goal.getGoalDesc());
            setDate(ps, 3, goal.getDeadline());
            ps.setString(4, goal.getPriority());
            ps.setString(5, goal.getSuccessMetric());
            ps.setString(6, goal.getStatus());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("addGoal", e);
            return false;
        }
    }

    @Override
    public boolean updateGoalProgress(int goalId, int progress, String status) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_PROGRESS)) {

            ps.setInt(1, progress);
            ps.setString(2, status);
            ps.setInt(3, goalId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updateGoalProgress", e);
            return false;
        }
    }

    @Override
    public Goals getGoalById(int goalId) {
        return fetchSingle(SELECT_BY_ID, goalId);
    }

    @Override
    public List<Goals> getGoalsByEmpId(int empId) {
        return fetchList(SELECT_BY_EMP, empId);
    }

    @Override
    public boolean deleteGoal(int goalId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, goalId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("deleteGoal", e);
            return false;
        }
    }

    // ================= FETCH HELPERS =================

    private Goals fetchSingle(String sql, int param) {

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

    private List<Goals> fetchList(String sql, int param) {

        List<Goals> list = new ArrayList<>();

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

    private Goals mapRow(ResultSet rs) throws SQLException {

        Goals g = new Goals();

        g.setGoalId(rs.getInt("goal_id"));
        g.setEmpId(rs.getInt("emp_id"));
        g.setGoalDesc(rs.getString("goal_desc"));
        g.setDeadline(toLocalDate(rs.getDate("deadline")));
        g.setPriority(rs.getString("priority"));
        g.setSuccessMetric(rs.getString("success_metric"));
        g.setProgress(rs.getInt("progress"));
        g.setStatus(rs.getString("status"));

        return g;
    }

    // ================= SMALL UTILITIES =================

    private LocalDate toLocalDate(Date date) {
        return date != null ? date.toLocalDate() : null;
    }

    private void setDate(PreparedStatement ps, int index, LocalDate date) throws SQLException {
        if (date != null) {
            ps.setDate(index, Date.valueOf(date));
        } else {
            ps.setNull(index, Types.DATE);
        }
    }

    private void logError(String method, SQLException e) {
        System.err.println("[GoalsDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
