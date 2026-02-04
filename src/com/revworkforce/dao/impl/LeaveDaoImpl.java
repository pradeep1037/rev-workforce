package com.revworkforce.dao.impl;

import com.revworkforce.dao.ILeaveDao;
import com.revworkforce.model.LeaveApplication;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LeaveDaoImpl implements ILeaveDao {

    // ================= SQL QUERIES =================

    private static final String INSERT = """
        INSERT INTO leave_application
        (emp_id, leave_type_id, start_date, end_date, reason, status, applied_on)
        VALUES (?, ?, ?, ?, ?, 'PENDING', SYSTIMESTAMP)
    """;

    private static final String APPROVE = """
        UPDATE leave_application
        SET status = 'APPROVED',
            approved_by = ?,
            manager_comment = ?
        WHERE leave_id = ?
    """;

    private static final String REJECT = """
        UPDATE leave_application
        SET status = 'REJECTED',
            approved_by = ?,
            manager_comment = ?
        WHERE leave_id = ?
    """;

    private static final String SELECT_BY_EMP =
            "SELECT * FROM leave_application WHERE emp_id = ? ORDER BY applied_on DESC";

    private static final String SELECT_PENDING_BY_MANAGER = """
        SELECT la.*
        FROM leave_application la
        JOIN employee e ON la.emp_id = e.emp_id
        WHERE e.manager_id = ? AND la.status = 'PENDING'
        ORDER BY la.applied_on
    """;

    private static final String SELECT_BY_ID =
            "SELECT * FROM leave_application WHERE leave_id = ?";

    // ================= DAO METHODS =================

    @Override
    public boolean applyLeave(LeaveApplication leave) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {

            ps.setInt(1, leave.getEmpId());
            ps.setInt(2, leave.getLeaveTypeId());
            setDate(ps, 3, leave.getStartDate());
            setDate(ps, 4, leave.getEndDate());
            ps.setString(5, leave.getReason());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("applyLeave", e);
            return false;
        }
    }

    @Override
    public boolean approveLeave(int leaveId, int managerId, String comment) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(APPROVE)) {

            ps.setInt(1, managerId);
            ps.setString(2, comment);
            ps.setInt(3, leaveId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("approveLeave", e);
            return false;
        }
    }

    @Override
    public boolean rejectLeave(int leaveId, int managerId, String comment) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(REJECT)) {

            ps.setInt(1, managerId);
            ps.setString(2, comment);
            ps.setInt(3, leaveId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("rejectLeave", e);
            return false;
        }
    }

    @Override
    public List<LeaveApplication> getLeavesByEmployee(int empId) {
        return fetchList(SELECT_BY_EMP, empId);
    }

    @Override
    public List<LeaveApplication> getPendingLeavesByManager(int managerId) {
        return fetchList(SELECT_PENDING_BY_MANAGER, managerId);
    }

    @Override
    public LeaveApplication getLeaveById(int leaveId) {
        return fetchSingle(SELECT_BY_ID, leaveId);
    }

    // ================= FETCH HELPERS =================

    private LeaveApplication fetchSingle(String sql, int param) {

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

    private List<LeaveApplication> fetchList(String sql, int param) {

        List<LeaveApplication> list = new ArrayList<>();

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

    private LeaveApplication mapRow(ResultSet rs) throws SQLException {

        LeaveApplication leave = new LeaveApplication();

        leave.setLeaveId(rs.getInt("leave_id"));
        leave.setEmpId(rs.getInt("emp_id"));
        leave.setLeaveTypeId(rs.getInt("leave_type_id"));

        if (rs.getDate("start_date") != null) {
            leave.setStartDate(rs.getDate("start_date").toLocalDate());
        }

        if (rs.getDate("end_date") != null) {
            leave.setEndDate(rs.getDate("end_date").toLocalDate());
        }

        // ✅ THIS WAS MISSING
        leave.setReason(rs.getString("reason"));

        leave.setStatus(rs.getString("status"));

        // ✅ For employee to see rejection reason
        leave.setManagerComment(rs.getString("manager_comment"));

        // Optional but correct
        if (rs.getTimestamp("applied_on") != null) {
            leave.setAppliedOn(rs.getTimestamp("applied_on").toLocalDateTime());
        }

        int approvedBy = rs.getInt("approved_by");
        leave.setApprovedBy(rs.wasNull() ? null : approvedBy);

        return leave;
    }


    // ================= SMALL UTILITIES =================

    private LocalDate toLocalDate(Date date) {
        return date != null ? date.toLocalDate() : null;
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private void setDate(PreparedStatement ps, int index, LocalDate date) throws SQLException {
        if (date != null) {
            ps.setDate(index, Date.valueOf(date));
        } else {
            ps.setNull(index, Types.DATE);
        }
    }

    private void logError(String method, SQLException e) {
        System.err.println("[LeaveDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
