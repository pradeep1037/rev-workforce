package com.revworkforce.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // 1. Database configuration
    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";

    private static final String USERNAME = "rev_workforce";
    private static final String PASSWORD = "rev_workforce123";

    // 2. Load Oracle JDBC Driver (static block)
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC Driver not found", e);
        }
    }

    // 3. Get connection method
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // 4. Prevent instantiation
    private DBConnection() {}
}
