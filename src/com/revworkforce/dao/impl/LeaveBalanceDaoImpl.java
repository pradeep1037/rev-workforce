package com.revworkforce.dao.impl;

import com.revworkforce.dao.ILeaveBalanceDao;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveBalanceDaoImpl implements ILeaveBalanceDao {

    // ================= SQL QUERIES =================

    private static final String SELECT_BALANCE =
            "SELECT * FROM leave_balance WHERE emp_id = ? AND leave_type_id = ?";

    private static final String UPDATE_BALANCE =
            "UPDATE leave_balance " +
                    "SET balance_days = balance_days + ? " +
                    "WHERE emp_id = ? AND leave_type_id = ?";

    private static final String INITIALIZE_BALANCE =
            "INSERT INTO leave_balance (emp_id, leave_type_id, balance_days) " +
                    "SELECT ?, leave_type_id, max_per_year FROM leave_type";

    // ================= DAO METHODS =================

    @Override
    public LeaveBalance getBalance(int empId, int leaveTypeId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BALANCE)) {

            ps.setInt(1, empId);
            ps.setInt(2, leaveTypeId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }

        } catch (SQLException e) {
            logError("getBalance", e);
            return null;
        }
    }

    @Override
    public boolean updateBalance(int empId, int leaveTypeId, int days) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_BALANCE)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);
            ps.setInt(3, leaveTypeId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updateBalance", e);
            return false;
        }
    }

    @Override
    public boolean initializeBalance(int empId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INITIALIZE_BALANCE)) {

            ps.setInt(1, empId);
            return ps.executeUpdate() > 0; // multiple rows expected

        } catch (SQLException e) {
            logError("initializeBalance", e);
            return false;
        }
    }

    // ================= ROW MAPPER =================

    private LeaveBalance mapRow(ResultSet rs) throws SQLException {

        LeaveBalance lb = new LeaveBalance();

        lb.setBalanceId(rs.getInt("balance_id"));
        lb.setEmpId(rs.getInt("emp_id"));
        lb.setLeaveTypeId(rs.getInt("leave_type_id"));
        lb.setBalanceDays(rs.getInt("balance_days"));

        return lb;
    }

    // ================= LOGGING =================

    private void logError(String method, SQLException e) {
        System.err.println("[LeaveBalanceDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
