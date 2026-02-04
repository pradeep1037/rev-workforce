package com.revworkforce.dao.impl;

import com.revworkforce.dao.INotificationsDao;
import com.revworkforce.model.Notifications;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDaoImpl implements INotificationsDao {

    // ================= SQL QUERIES =================

    private static final String INSERT = """
        INSERT INTO notifications
        (user_id, message, type, is_read, created_at)
        VALUES (?, ?, ?, ?, ?)
    """;

    private static final String SELECT_BY_ID =
            "SELECT * FROM notifications WHERE notification_id = ?";

    private static final String SELECT_BY_USER =
            "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";

    private static final String MARK_AS_READ =
            "UPDATE notifications SET is_read = 1 WHERE notification_id = ?";

    private static final String DELETE =
            "DELETE FROM notifications WHERE notification_id = ?";

    // ================= DAO METHODS =================

    @Override
    public boolean addNotification(Notifications notification) {

        String sql = """
        INSERT INTO notifications
        (user_id, message, type, is_read, created_at)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notification.getUserId());
            ps.setString(2, notification.getMessage());

            // ✅ FORCE DEFAULT TYPE
            ps.setString(3,
                    notification.getType() != null
                            ? notification.getType()
                            : "SYSTEM"
            );

            ps.setBoolean(4, notification.isRead());

            // ✅ FORCE CREATED_AT
            ps.setTimestamp(5,
                    notification.getCreatedAt() != null
                            ? Timestamp.valueOf(notification.getCreatedAt())
                            : Timestamp.valueOf(LocalDateTime.now())
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Notifications getNotificationById(int notificationId) {
        return fetchSingle(SELECT_BY_ID, notificationId);
    }

    @Override
    public List<Notifications> getNotificationsByUserId(int userId) {
        return fetchList(SELECT_BY_USER, userId);
    }

    @Override
    public boolean markAsRead(int notificationId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(MARK_AS_READ)) {

            ps.setInt(1, notificationId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("markAsRead", e);
            return false;
        }
    }

    @Override
    public boolean deleteNotification(int notificationId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, notificationId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("deleteNotification", e);
            return false;
        }
    }

    // ================= FETCH HELPERS =================

    private Notifications fetchSingle(String sql, int param) {

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

    private List<Notifications> fetchList(String sql, int param) {

        List<Notifications> list = new ArrayList<>();

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

    private Notifications mapRow(ResultSet rs) throws SQLException {
        Notifications n = new Notifications();
        n.setNotificationId(rs.getInt("notification_id"));
        n.setUserId(rs.getInt("user_id"));
        n.setMessage(rs.getString("message")); // 🔥 REQUIRED
        n.setType(rs.getString("type"));
        n.setRead(rs.getBoolean("is_read"));

        if (rs.getTimestamp("created_at") != null) {
            n.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        return n;
    }


    // ================= SMALL UTILITIES =================

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private Timestamp toTimestamp(LocalDateTime ldt) {
        return ldt != null ? Timestamp.valueOf(ldt) : null;
    }

    private void logError(String method, SQLException e) {
        System.err.println("[NotificationsDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
