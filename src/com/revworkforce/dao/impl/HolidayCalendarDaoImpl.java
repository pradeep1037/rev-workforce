package com.revworkforce.dao.impl;

import com.revworkforce.dao.IHolidayCalendarDao;
import com.revworkforce.model.HolidayCalendar;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayCalendarDaoImpl implements IHolidayCalendarDao {

    // ================= SQL QUERIES =================

    private static final String INSERT = """
        INSERT INTO holiday_calendar (holiday_date, description)
        VALUES (?, ?)
    """;

    private static final String SELECT_BY_ID =
            "SELECT * FROM holiday_calendar WHERE holiday_id = ?";

    private static final String SELECT_ALL =
            "SELECT * FROM holiday_calendar ORDER BY holiday_date";

    private static final String DELETE =
            "DELETE FROM holiday_calendar WHERE holiday_id = ?";

    // ================= CRUD METHODS =================

    @Override
    public boolean addHoliday(HolidayCalendar holiday) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {

            setDate(ps, 1, holiday.getHolidayDate());
            ps.setString(2, holiday.getDescription());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("addHoliday", e);
            return false;
        }
    }

    @Override
    public HolidayCalendar getHolidayById(int holidayId) {
        return fetchSingle(SELECT_BY_ID, holidayId);
    }

    @Override
    public List<HolidayCalendar> getAllHolidays() {
        return fetchList(SELECT_ALL);
    }

    @Override
    public boolean deleteHoliday(int holidayId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, holidayId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("deleteHoliday", e);
            return false;
        }
    }

    // ================= FETCH HELPERS =================

    private HolidayCalendar fetchSingle(String sql, int param) {

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

    private List<HolidayCalendar> fetchList(String sql) {

        List<HolidayCalendar> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logError("fetchList", e);
        }

        return list;
    }

    // ================= ROW MAPPER =================

    private HolidayCalendar mapRow(ResultSet rs) throws SQLException {

        HolidayCalendar h = new HolidayCalendar();

        h.setHolidayId(rs.getInt("holiday_id"));
        h.setHolidayDate(toLocalDate(rs.getDate("holiday_date")));
        h.setDescription(rs.getString("description"));

        return h;
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
        System.err.println("[HolidayCalendarDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
