package com.revworkforce.dao.impl;

import com.revworkforce.dao.IEventsDao;
import com.revworkforce.model.Events;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventsDaoImpl implements IEventsDao {

    // ================= SQL QUERIES =================

    private static final String INSERT = """
        INSERT INTO events (emp_id, event_type, event_date)
        VALUES (?, ?, ?)
    """;

    private static final String SELECT_BY_ID =
            "SELECT * FROM events WHERE event_id = ?";

    private static final String SELECT_BY_EMP =
            "SELECT * FROM events WHERE emp_id = ? ORDER BY event_date DESC";

    private static final String DELETE =
            "DELETE FROM events WHERE event_id = ?";

    // ================= CRUD METHODS =================

    @Override
    public boolean addEvent(Events event) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {

            ps.setInt(1, event.getEmpId());
            ps.setString(2, event.getEventType());
            setDate(ps, 3, event.getEventDate());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("addEvent", e);
            return false;
        }
    }

    @Override
    public Events getEventById(int eventId) {
        return fetchSingle(SELECT_BY_ID, eventId);
    }

    @Override
    public List<Events> getEventsByEmpId(int empId) {
        return fetchList(SELECT_BY_EMP, empId);
    }

    @Override
    public boolean deleteEvent(int eventId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, eventId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("deleteEvent", e);
            return false;
        }
    }

    // ================= FETCH HELPERS =================

    private Events fetchSingle(String sql, int param) {

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

    private List<Events> fetchList(String sql, int param) {

        List<Events> list = new ArrayList<>();

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

    private Events mapRow(ResultSet rs) throws SQLException {

        Events e = new Events();

        e.setEventId(rs.getInt("event_id"));
        e.setEmpId(rs.getInt("emp_id"));
        e.setEventType(rs.getString("event_type"));
        e.setEventDate(toLocalDate(rs.getDate("event_date")));

        return e;
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
        System.err.println("[EventsDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
