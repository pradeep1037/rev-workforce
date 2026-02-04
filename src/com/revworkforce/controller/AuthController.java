package com.revworkforce.controller;

import com.revworkforce.model.Users;
import com.revworkforce.service.IAuthService;

import java.util.Scanner;

public class AuthController {

    private final IAuthService authService;
    private final Scanner scanner;

    public AuthController(IAuthService authService) {
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }


    public Users login() {

        printLoginHeader();

        System.out.print("📧 Email    : ");
        String email = scanner.nextLine().trim();

        System.out.print("🔑 Password : ");
        String password = scanner.nextLine().trim();

        try {
            Users user = authService.login(email, password);

            System.out.println();
            System.out.println("✅ Login successful!");
            System.out.println("👤 Role : " + user.getRole());
            System.out.println("------------------------------------");

            return user;

        } catch (RuntimeException e) {

            System.out.println();
            System.out.println("❌ Login failed");
            System.out.println("Reason : " + e.getMessage());
            System.out.println("------------------------------------");

            return null;
        }
    }

    /* ================= UI Helper Methods ================= */

    private void printLoginHeader() {
        System.out.println();
        System.out.println("====================================");
        System.out.println("            USER LOGIN               ");
        System.out.println("====================================");
    }
}
