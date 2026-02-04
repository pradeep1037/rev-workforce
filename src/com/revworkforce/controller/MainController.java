package com.revworkforce.controller;

import com.revworkforce.model.Users;

import java.util.Scanner;

public class MainController {

    private final AdminController adminController;
    private final EmployeeController employeeController;
    private final ManagerController managerController;
    private final Scanner scanner = new Scanner(System.in);

    public MainController(
            AdminController adminController,
            EmployeeController employeeController,
            ManagerController managerController) {

        this.adminController = adminController;
        this.employeeController = employeeController;
        this.managerController = managerController;
    }

    public void routeUser(Users user) {

        if (user == null) return;

        System.out.println();
        System.out.println("====================================");
        System.out.println(" Logged in as : " + user.getRole());
        System.out.println("====================================");

        switch (user.getRole()) {

            case "ADMIN" -> adminController.start(user);

            case "EMPLOYEE" -> employeeController.start(user);

            case "MANAGER" -> managerMenuSelection(user);

            default ->
                    System.out.println("❌ Unknown role: " + user.getRole());
        }
    }

    /* ================= MANAGER ROLE SELECTION ================= */

    private void managerMenuSelection(Users user) {

        while (true) {

            printManagerChoiceMenu();

            System.out.print("👉 Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> employeeController.start(user);
                case 2 -> managerController.start(user);
                case 0 -> {
                    System.out.println("\n🔒 Logging out...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    /* ================= UI HELPERS ================= */

    private void printManagerChoiceMenu() {
        System.out.println("\n====================================");
        System.out.println("        MANAGER ROLE OPTIONS         ");
        System.out.println("====================================");
        System.out.println("1️⃣  Employee Menu");
        System.out.println("2️⃣  Manager Menu");
        System.out.println("0️⃣  Logout");
        System.out.println("====================================");
    }
}
