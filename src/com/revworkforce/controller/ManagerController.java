package com.revworkforce.controller;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveApplication;
import com.revworkforce.model.Users;
import com.revworkforce.service.IEmployeeService;
import com.revworkforce.service.IManagerService;

import java.util.List;
import java.util.Scanner;

public class ManagerController {

    private final IManagerService managerService;
    private final IEmployeeService employeeService;
    private final Scanner scanner;

    public ManagerController(IManagerService managerService,
                             IEmployeeService employeeService) {
        this.managerService = managerService;
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
    }

    public void start(Users user) {

        Employee manager = employeeService.getMyProfile(user.getUserId());

        if (manager == null) {
            System.out.println("❌ Manager profile not found.");
            return;
        }

        int managerId = manager.getEmpId();

        while (true) {
            printMenu();

            System.out.print("👉 Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> viewPendingLeaves(managerId);
                case 2 -> approveLeave(managerId);
                case 3 -> rejectLeave(managerId);
                case 0 -> {
                    System.out.println("\n🔒 Logging out from Manager Menu...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    /* ================= MENU ================= */

    private void printMenu() {
        System.out.println("\n====================================");
        System.out.println("           MANAGER MENU              ");
        System.out.println("====================================");
        System.out.println("1️⃣  View Pending Leave Requests");
        System.out.println("2️⃣  Approve Leave");
        System.out.println("3️⃣  Reject Leave");
        System.out.println("0️⃣  Logout");
        System.out.println("====================================");
    }

    /* ================= FEATURES ================= */

    private void viewPendingLeaves(int managerId) {

        System.out.println("\n--- Pending Leave Requests ---");

        List<LeaveApplication> leaves =
                managerService.getPendingLeaveRequests(managerId);

        if (leaves.isEmpty()) {
            System.out.println("No pending leave requests.");
            return;
        }

        for (LeaveApplication l : leaves) {
            printLeave(l);
        }
    }

    private void approveLeave(int managerId) {

        System.out.println("\n--- Approve Leave ---");

        System.out.print("Enter Leave ID : ");
        int leaveId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Approval Comment : ");
        String comment = scanner.nextLine();

        try {
            managerService.approveLeave(leaveId, managerId, comment);
            System.out.println("✅ Leave approved successfully.");
        } catch (RuntimeException e) {
            System.out.println("❌ Approval failed: " + e.getMessage());
        }
    }

    private void rejectLeave(int managerId) {

        System.out.println("\n--- Reject Leave ---");

        System.out.print("Enter Leave ID : ");
        int leaveId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Rejection Comment : ");
        String comment = scanner.nextLine();

        try {
            managerService.rejectLeave(leaveId, managerId, comment);
            System.out.println("❌ Leave rejected successfully.");
        } catch (RuntimeException e) {
            System.out.println("❌ Rejection failed: " + e.getMessage());
        }
    }

    /* ================= PRINT HELPERS ================= */

    private void printLeave(LeaveApplication l) {
        System.out.println("------------------------------------");
        System.out.println("Leave ID   : " + l.getLeaveId());
        System.out.println("Employee ID: " + l.getEmpId());
        System.out.println("Leave Type : " + l.getLeaveTypeId());
        System.out.println("From       : " + l.getStartDate());
        System.out.println("To         : " + l.getEndDate());
        System.out.println("Reason     : " + l.getReason());
        System.out.println("Status     : " + formatStatus(l.getStatus()));
        System.out.println("------------------------------------");
    }

    private String formatStatus(String status) {
        return switch (status) {
            case "PENDING" -> "⏳ PENDING";
            case "APPROVED" -> "✅ APPROVED";
            case "REJECTED" -> "❌ REJECTED";
            default -> status;
        };
    }
}
