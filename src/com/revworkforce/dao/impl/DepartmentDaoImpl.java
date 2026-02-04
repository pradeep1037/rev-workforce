package com.revworkforce.dao.impl;

import com.revworkforce.dao.IDepartmentDao;
import com.revworkforce.model.Department;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImpl implements IDepartmentDao {

    // ================= SQL QUERIES =================

    private static final String INSERT =
            "INSERT INTO department (department_name) VALUES (?)";

    private static final String UPDATE =
            "UPDATE department SET department_name = ? WHERE department_id = ?";

    private static final String SELECT_BY_ID =
            "SELECT department_id, department_name FROM department WHERE department_id = ?";

    private static final String SELECT_ALL =
            "SELECT department_id, department_name FROM department";

    private static final String DELETE =
            "DELETE FROM department WHERE department_id = ?";

    private static final String EXISTS_BY_NAME =
            "SELECT 1 FROM department WHERE LOWER(department_name) = LOWER(?)";

    // ================= CRUD METHODS =================

    @Override
    public boolean addDepartment(Department department) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {

            ps.setString(1, department.getDepartmentName());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("addDepartment", e);
            return false;
        }
    }

    @Override
    public boolean updateDepartment(Department department) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {

            ps.setString(1, department.getDepartmentName());
            ps.setInt(2, department.getDepartmentId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("updateDepartment", e);
            return false;
        }
    }

    @Override
    public Department getDepartmentById(int departmentId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, departmentId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }

        } catch (SQLException e) {
            logError("getDepartmentById", e);
            return null;
        }
    }

    @Override
    public List<Department> getAllDepartments() {

        List<Department> departments = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                departments.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logError("getAllDepartments", e);
        }

        return departments;
    }

    @Override
    public boolean deleteDepartment(int departmentId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, departmentId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            logError("deleteDepartment", e);
            return false;
        }
    }

    @Override
    public boolean existsByName(String departmentName) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(EXISTS_BY_NAME)) {

            ps.setString(1, departmentName);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // existence check
            }

        } catch (SQLException e) {
            logError("existsByName", e);
            return false;
        }
    }

    // ================= ROW MAPPER =================

    private Department mapRow(ResultSet rs) throws SQLException {
        return new Department(
                rs.getInt("department_id"),
                rs.getString("department_name")
        );
    }

    // ================= ERROR LOGGING =================

    private void logError(String method, SQLException e) {
        System.err.println("[DepartmentDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
