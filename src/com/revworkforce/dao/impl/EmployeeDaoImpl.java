package com.revworkforce.dao.impl;

import com.revworkforce.dao.IEmployeeDao;
import com.revworkforce.model.Employee;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements IEmployeeDao {

    // ================= SQL QUERIES =================

    private static final String INSERT = """
        INSERT INTO employee (
            user_id, first_name, last_name, phone, address,
            emergency_contact, dob, joining_date,
            department_id, designation, manager_id,
            salary, status
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String UPDATE = """
        UPDATE employee SET
            first_name = ?, last_name = ?, phone = ?, address = ?,
            emergency_contact = ?, department_id = ?, designation = ?,
            manager_id = ?, salary = ?, status = ?
        WHERE emp_id = ?
    """;

    private static final String SELECT_BY_ID =
            "SELECT * FROM employee WHERE emp_id = ?";

    private static final String SELECT_BY_USER_ID =
            "SELECT * FROM employee WHERE user_id = ?";

    private static final String SELECT_BY_DEPARTMENT =
            "SELECT * FROM employee WHERE department_id = ?";

    private static final String SELECT_BY_MANAGER =
            "SELECT * FROM employee WHERE manager_id = ?";

    private static final String SELECT_ALL =
            "SELECT * FROM employee";

    private static final String UPDATE_MANAGER =
            "UPDATE employee SET manager_id = ? WHERE emp_id = ?";

    private static final String UPDATE_STATUS =
            "UPDATE employee SET status = ? WHERE emp_id = ?";

    // ================= CRUD METHODS =================

//    @Override
//    public int addEmployee(Employee e) {
//
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(INSERT)) {
//
//            ps.setInt(1, e.getUserId());
//            ps.setString(2, e.getFirstName());
//            ps.setString(3, e.getLastName());
//            ps.setString(4, e.getPhone());
//            ps.setString(5, e.getAddress());
//            ps.setString(6, e.getEmergencyContact());
//
//            setDate(ps, 7, e.getDob());
//            setDate(ps, 8, e.getJoiningDate());
//
//            ps.setInt(9, e.getDepartmentId());
//            ps.setString(10, e.getDesignation());
//            setInteger(ps, 11, e.getManagerId());
//
//            ps.setBigDecimal(12, e.getSalary());
//            ps.setString(13, e.getStatus());
//
//            return ps.executeUpdate() == 1;
//
//        } catch (SQLException ex) {
//            logError("addEmployee", ex);
//            return false;
//        }
//    }

    @Override
    public int addEmployee(Employee employee) {

        String sql = """
        INSERT INTO employee (
            user_id, first_name, last_name, phone, address,
            emergency_contact, dob, joining_date,
            department_id, designation, manager_id,
            salary, status
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql, new String[]{"emp_id"})) {

            ps.setInt(1, employee.getUserId()); // 🔥 THIS MUST EXIST
            ps.setString(2, employee.getFirstName());
            ps.setString(3, employee.getLastName());
            ps.setString(4, employee.getPhone());
            ps.setString(5, employee.getAddress());
            ps.setString(6, employee.getEmergencyContact());

            ps.setDate(7, employee.getDob() != null ? Date.valueOf(employee.getDob()) : null);
            ps.setDate(8, employee.getJoiningDate() != null ? Date.valueOf(employee.getJoiningDate()) : null);

            ps.setInt(9, employee.getDepartmentId());
            ps.setString(10, employee.getDesignation());

            if (employee.getManagerId() != null) {
                ps.setInt(11, employee.getManagerId());
            } else {
                ps.setNull(11, Types.INTEGER);
            }

            ps.setBigDecimal(12, employee.getSalary());
            ps.setString(13, employee.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ✅ GENERATED emp_id
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public boolean updateEmployee(Employee e) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {

            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setString(3, e.getPhone());
            ps.setString(4, e.getAddress());
            ps.setString(5, e.getEmergencyContact());
            ps.setInt(6, e.getDepartmentId());
            ps.setString(7, e.getDesignation());
            setInteger(ps, 8, e.getManagerId());
            ps.setBigDecimal(9, e.getSalary());
            ps.setString(10, e.getStatus());
            ps.setInt(11, e.getEmpId());

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            logError("updateEmployee", ex);
            return false;
        }
    }

    @Override
    public Employee getEmployeeById(int empId) {
        return fetchSingle(SELECT_BY_ID, empId);
    }

    @Override
    public Employee getEmployeeByUserId(int userId) {
        return fetchSingle(SELECT_BY_USER_ID, userId);
    }

    @Override
    public List<Employee> getEmployeesByDepartment(int departmentId) {
        return fetchList(SELECT_BY_DEPARTMENT, departmentId);
    }

    @Override
    public List<Employee> getEmployeesByManager(int managerId) {
        return fetchList(SELECT_BY_MANAGER, managerId);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return fetchList(SELECT_ALL, null);
    }

    @Override
    public boolean updateManager(int empId, int managerId) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_MANAGER)) {

            ps.setInt(1, managerId);
            ps.setInt(2, empId);

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            logError("updateManager", ex);
            return false;
        }
    }

    @Override
    public boolean updateEmployeeStatus(int empId, boolean isActive) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_STATUS)) {

            ps.setString(1, isActive ? "ACTIVE" : "INACTIVE");
            ps.setInt(2, empId);

            return ps.executeUpdate() == 1;

        } catch (SQLException ex) {
            logError("updateEmployeeStatus", ex);
            return false;
        }
    }

    // ================= FETCH HELPERS =================

    private Employee fetchSingle(String sql, Integer param) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (param != null) ps.setInt(1, param);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }

        } catch (SQLException ex) {
            logError("fetchSingle", ex);
            return null;
        }
    }

    private List<Employee> fetchList(String sql, Integer param) {

        List<Employee> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (param != null) ps.setInt(1, param);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException ex) {
            logError("fetchList", ex);
        }

        return list;
    }

    // ================= ROW MAPPER =================

    private Employee mapRow(ResultSet rs) throws SQLException {

        Employee e = new Employee();

        e.setEmpId(rs.getInt("emp_id"));
        e.setUserId(rs.getInt("user_id"));
        e.setFirstName(rs.getString("first_name"));
        e.setLastName(rs.getString("last_name"));
        e.setPhone(rs.getString("phone"));
        e.setAddress(rs.getString("address"));
        e.setEmergencyContact(rs.getString("emergency_contact"));

        e.setDob(toLocalDate(rs.getDate("dob")));
        e.setJoiningDate(toLocalDate(rs.getDate("joining_date")));

        e.setDepartmentId(rs.getInt("department_id"));
        e.setDesignation(rs.getString("designation"));

        Integer managerId = rs.getInt("manager_id");
        e.setManagerId(rs.wasNull() ? null : managerId);

        e.setSalary(rs.getBigDecimal("salary"));
        e.setStatus(rs.getString("status"));

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

    private void setInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value != null) {
            ps.setInt(index, value);
        } else {
            ps.setNull(index, Types.INTEGER);
        }
    }

    private void logError(String method, SQLException e) {
        System.err.println("[EmployeeDaoImpl] Error in " + method);
        e.printStackTrace();
    }
}
