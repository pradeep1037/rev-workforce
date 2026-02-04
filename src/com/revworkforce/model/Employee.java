package com.revworkforce.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {

    private int empId;
    private int userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String emergencyContact;
    private LocalDate dob;
    private LocalDate joiningDate;
    private int departmentId;
    private String designation;
    private Integer managerId;
    private BigDecimal salary;
    private String status;

    public Employee() {}

    public Employee(int userId, String firstName, String lastName, String phone,
                    String address, String emergencyContact, LocalDate dob,
                    LocalDate joiningDate, int departmentId, String designation,
                    Integer managerId, BigDecimal salary, String status) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.dob = dob;
        this.joiningDate = joiningDate;
        this.departmentId = departmentId;
        this.designation = designation;
        this.managerId = managerId;
        this.salary = salary;
        this.status = status;
    }

    public Employee(int empId, int userId, String firstName, String lastName,
                    String phone, String address, String emergencyContact,
                    LocalDate dob, LocalDate joiningDate, int departmentId,
                    String designation, Integer managerId, BigDecimal salary,
                    String status) {
        this.empId = empId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.dob = dob;
        this.joiningDate = joiningDate;
        this.departmentId = departmentId;
        this.designation = designation;
        this.managerId = managerId;
        this.salary = salary;
        this.status = status;
    }

    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", userId=" + userId +
                ", name='" + firstName + " " + lastName + '\'' +
                ", departmentId=" + departmentId +
                ", designation='" + designation + '\'' +
                ", managerId=" + managerId +
                ", status='" + status + '\'' +
                '}';
    }
}
