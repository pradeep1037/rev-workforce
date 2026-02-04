package com.revworkforce.dao.impl;

import com.revworkforce.dao.IAnnouncementsDao;
import com.revworkforce.model.Announcements;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementsDaoImpl implements IAnnouncementsDao {

    // ================= SQL QUERIES =================

    private static final String INSERT =
            "INSERT INTO announcements (title, message, created_by, created_at) " +
                    "VALUES (?, ?, ?, SYSTIMESTAMP)";

    private static final String SELECT_ALL =
            "SELECT * FROM announcements ORDER BY created_at DESC";

    private static final String SELECT_BY_USER =
            "SELECT * FROM announcements WHERE created_by = ? ORDER BY created_at DESC";

    private static final String UPDATE =
            "UPDATE announcements SET title = ?, message = ? WHERE announcement_id = ?";

    private static final String DELETE =
            "DELETE FROM announcements WHERE announcement_id = ?";

    private static final String SELECT_BY_ROLE =
            "SELECT a.* FROM announcements a " +
                    "JOIN users u ON a.created_by = u.user_id " +
                    "WHERE u.role = ? " +
                    "ORDER BY a.created_at DESC";

    // ================= CRUD METHODS =================

    @Override
    public boolean createAnnouncement(Announcements announcement) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {

            ps.setString(1, announcement.getTitle());
            ps.setString(2, announcement.getMessage());
            ps.setInt(3, announcement.getCreatedBy());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("createAnnouncement", e);
            return false;
        }
    }

    @Override
    public List<Announcements> getAllAnnouncements() {

        List<Announcements> announcements = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                announcements.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logError("getAllAnnouncements", e);
        }

        return announcements;
    }

    @Override
    public List<Announcements> getAnnouncementsByUser(int userId) {

        List<Announcements> announcements = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_USER)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    announcements.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            logError("getAnnouncementsByUser", e);
        }

        return announcements;
    }

    @Override
    public boolean updateAnnouncement(Announcements announcement) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, announcement.getTitle());
            ps.setString(2, announcement.getMessage());
            ps.setInt(3, announcement.getAnnouncementId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updateAnnouncement", e);
            return false;
        }
    }

    @Override
    public boolean deleteAnnouncement(int announcementId) {

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, announcementId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("deleteAnnouncement", e);
            return false;
        }
    }

    @Override
    public List<Announcements> getAnnouncementsByRole(String role) {

        List<Announcements> announcements = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ROLE)) {

            ps.setString(1, role);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    announcements.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            logError("getAnnouncementsByRole", e);
        }

        return announcements;
    }

    // ================= ROW MAPPER =================

    private Announcements mapRow(ResultSet rs) throws SQLException {

        Announcements a = new Announcements();

        a.setAnnouncementId(rs.getInt("announcement_id"));
        a.setTitle(rs.getString("title"));
        a.setMessage(rs.getString("message"));
        a.setCreatedBy(rs.getInt("created_by"));
        a.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));

        return a;
    }

    // ================= SMALL UTILITIES =================

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private void logError(String method, SQLException e) {
        System.err.println("[AnnouncementsDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
