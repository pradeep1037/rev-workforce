package com.revworkforce.controller;

import com.revworkforce.model.*;
import com.revworkforce.service.IAdminService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class AdminController {

    private final IAdminService adminService;
    private final Scanner scanner;
    private final Users loggedInUser;

    public AdminController(Users loggedInUser, IAdminService adminService) {
        this.loggedInUser = loggedInUser;
        this.adminService = adminService;
        this.scanner = new Scanner(System.in);
    }

    public void start(Users admin) {

        while (true) {
            printMenu();

            System.out.print("👉 Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> assignManager();
                case 3 -> updateEmployeeStatus();
                case 4 -> viewEmployeeProfile();
                case 5 -> addDepartment();
                case 6 -> viewDepartments();
                case 7 -> addHoliday();
                case 8 -> createAnnouncement();
                case 0 -> {
                    System.out.println("\n🔒 Logging out Admin...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    /* ================= MENU ================= */

    private void printMenu() {
        System.out.println("\n====================================");
        System.out.println("            ADMIN MENU               ");
        System.out.println("====================================");
        System.out.println("1️⃣  Add Employee");
        System.out.println("2️⃣  Assign Manager");
        System.out.println("3️⃣  Activate / Deactivate Employee");
        System.out.println("4️⃣  View Employee Profile");
        System.out.println("5️⃣  Add Department");
        System.out.println("6️⃣  View Departments");
        System.out.println("7️⃣  Add Holiday");
        System.out.println("8️⃣  Create Announcement");
        System.out.println("0️⃣  Logout");
        System.out.println("====================================");
    }

    /* ================= FEATURES ================= */

    private void addEmployee() {

        System.out.println("\n--- Add New Employee ---");

        Users user = new Users();
        Employee emp = new Employee();

        System.out.print("📧 Email           : ");
        user.setEmail(scanner.nextLine());

        System.out.print("🔑 Password        : ");
        user.setPasswordHash(scanner.nextLine());

        System.out.print("👤 Role (EMPLOYEE / MANAGER): ");
        user.setRole(scanner.nextLine().toUpperCase());

        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        System.out.print("🧑 First Name      : ");
        emp.setFirstName(scanner.nextLine());

        System.out.print("🧑 Last Name       : ");
        emp.setLastName(scanner.nextLine());

        System.out.print("🏢 Department ID   : ");
        emp.setDepartmentId(scanner.nextInt());
        scanner.nextLine();

        System.out.print("💼 Designation     : ");
        emp.setDesignation(scanner.nextLine());

        emp.setStatus("ACTIVE");
        emp.setJoiningDate(LocalDate.now());

        try {
            adminService.addEmployee(user, emp);
            System.out.println("✅ Employee added successfully.");
        } catch (RuntimeException e) {
            System.out.println("❌ Failed to add employee: " + e.getMessage());
        }
    }

    private void assignManager() {

        System.out.println("\n--- Assign Manager ---");

        System.out.print("Employee ID : ");
        int empId = scanner.nextInt();

        System.out.print("Manager ID  : ");
        int managerId = scanner.nextInt();
        scanner.nextLine();

        try {
            adminService.assignManager(empId, managerId);
            System.out.println("✅ Manager assigned successfully.");
        } catch (RuntimeException e) {
            System.out.println("❌ Assignment failed: " + e.getMessage());
        }
    }

    private void updateEmployeeStatus() {

        System.out.println("\n--- Update Employee Status ---");

        System.out.print("Employee ID : ");
        int empId = scanner.nextInt();

        System.out.print("Activate? (true / false): ");
        boolean isActive = scanner.nextBoolean();
        scanner.nextLine();

        adminService.updateEmployeeStatus(empId, isActive);
        System.out.println("✅ Employee status updated.");
    }

    private void viewEmployeeProfile() {

        System.out.println("\n--- Employee Profile ---");

        System.out.print("Employee ID : ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        Employee emp = adminService.getEmployeeProfile(empId);

        if (emp == null) {
            System.out.println("❌ Employee not found.");
            return;
        }

        printEmployee(emp);
    }

    private void addDepartment() {

        System.out.println("\n--- Add Department ---");

        Department dept = new Department();

        System.out.print("Department Name : ");
        dept.setDepartmentName(scanner.nextLine());

        try {
            adminService.addDepartment(dept);
            System.out.println("✅ Department added.");
        } catch (RuntimeException e) {
            System.out.println("❌ Failed: " + e.getMessage());
        }
    }

    private void viewDepartments() {

        System.out.println("\n--- Department List ---");

        List<Department> departments = adminService.getAllDepartments();

        if (departments.isEmpty()) {
            System.out.println("No departments found.");
            return;
        }

        System.out.printf("%-5s %-20s%n", "ID", "Department Name");
        System.out.println("-----------------------------");

        for (Department d : departments) {
            System.out.printf("%-5d %-20s%n",
                    d.getDepartmentId(),
                    d.getDepartmentName());
        }
    }

    private void addHoliday() {

        System.out.println("\n--- Add Holiday ---");

        HolidayCalendar holiday = new HolidayCalendar();

        System.out.print("📅 Holiday Date (yyyy-mm-dd): ");
        holiday.setHolidayDate(LocalDate.parse(scanner.nextLine()));

        System.out.print("📝 Description              : ");
        holiday.setDescription(scanner.nextLine());

        adminService.addHoliday(holiday);
        System.out.println("✅ Holiday added.");
    }

    private boolean createAnnouncement() {

        System.out.println("\n--- Create Announcement ---");

        System.out.print("📢 Title   : ");
        String title = scanner.nextLine();

        System.out.print("📝 Message : ");
        String message = scanner.nextLine();

        Announcements announcement = new Announcements();
        announcement.setTitle(title);
        announcement.setMessage(message);
        announcement.setCreatedBy(loggedInUser.getUserId());

        boolean success = adminService.createAnnouncement(announcement);

        if (success) {
            System.out.println("✅ Announcement created successfully.");
        } else {
            System.out.println("❌ Failed to create announcement.");
        }
        return success;
    }

    /* ================= PRINT HELPERS ================= */

    private void printEmployee(Employee e) {
        System.out.println("------------------------------------");
        System.out.println("Employee ID   : " + e.getEmpId());
        System.out.println("Name          : " + e.getFirstName() + " " + e.getLastName());
        System.out.println("Department ID : " + e.getDepartmentId());
        System.out.println("Designation   : " + e.getDesignation());
        System.out.println("Status        : " + e.getStatus());
        System.out.println("------------------------------------");
    }
}
