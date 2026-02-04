package com.revworkforce.dao.impl;

import com.revworkforce.dao.IUserDao;
import com.revworkforce.model.Users;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements IUserDao {

    // ================= SQL QUERIES =================

    private static final String INSERT =
            "INSERT INTO users (email, password_hash, role, is_active, created_at) " +
                    "VALUES (?, ?, ?, ?, SYSTIMESTAMP)";

    private static final String SELECT_BY_EMAIL =
            "SELECT * FROM users WHERE email = ?";

    private static final String SELECT_BY_ID =
            "SELECT * FROM users WHERE user_id = ?";

    private static final String SELECT_FOR_LOGIN =
            "SELECT * FROM users WHERE email = ? AND is_active = 1";

    private static final String UPDATE_STATUS =
            "UPDATE users SET is_active = ? WHERE user_id = ?";

    private static final String UPDATE_LAST_LOGIN =
            "UPDATE users SET last_login = SYSTIMESTAMP WHERE user_id = ?";

    private static final String UPDATE_PASSWORD =
            "UPDATE users SET password_hash = ? WHERE user_id = ?";

    // ================= DAO METHODS =================

//    @Override
//    public boolean createUser(Users user) {
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(INSERT)) {
//
//            ps.setString(1, user.getEmail());
//            ps.setString(2, user.getPasswordHash());
//            ps.setString(3, user.getRole());
//            ps.setInt(4, user.isActive() ? 1 : 0);
//
//            return ps.executeUpdate() == 1;
//
//        } catch (SQLException e) {
//            logError("createUser", e);
//            return false;
//        }
//    }

    @Override
    public int createUser(Users user) {

        String sql =
                "INSERT INTO users (email, password_hash, role, is_active, created_at) " +
                        "VALUES (?, ?, ?, ?, SYSTIMESTAMP)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql, new String[]{"user_id"})) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.isActive() ? 1 : 0);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ✅ GENERATED user_id
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public Users getUserByEmail(String email) {
        return fetchSingle(SELECT_BY_EMAIL, email);
    }

    @Override
    public Users getUserById(int userId) {
        return fetchSingle(SELECT_BY_ID, userId);
    }

    @Override
    public Users getUserForLogin(String email) {
        return fetchSingle(SELECT_FOR_LOGIN, email);
    }

    @Override
    public boolean updateUserStatus(int userId, boolean isActive) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS)) {

            ps.setInt(1, isActive ? 1 : 0);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updateUserStatus", e);
            return false;
        }
    }

    @Override
    public boolean updateLastLogin(int userId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_LAST_LOGIN)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updateLastLogin", e);
            return false;
        }
    }

    @Override
    public boolean updatePassword(int userId, String passwordHash) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_PASSWORD)) {

            ps.setString(1, passwordHash);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updatePassword", e);
            return false;
        }
    }

    @Override
    public List<Users> getAllActiveUsers() {

        List<Users> users = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE is_active = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs)); // reuse existing mapper
            }

        } catch (SQLException e) {
            logError("getAllActiveUsers", e);
        }

        return users;
    }


    // ================= FETCH HELPER =================

    private Users fetchSingle(String sql, Object param) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (param instanceof Integer) {
                ps.setInt(1, (Integer) param);
            } else {
                ps.setString(1, (String) param);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }

        } catch (SQLException e) {
            logError("fetchSingle", e);
            return null;
        }
    }

    // ================= ROW MAPPER =================

    private Users mapRow(ResultSet rs) throws SQLException {

        Users u = new Users();

        u.setUserId(rs.getInt("user_id"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        u.setActive(rs.getInt("is_active") == 1);

        u.setLastLogin(toLocalDateTime(rs.getTimestamp("last_login")));
        u.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));

        return u;
    }

    // ================= SMALL UTILITIES =================

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private void logError(String method, SQLException e) {
        System.err.println("[UserDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
