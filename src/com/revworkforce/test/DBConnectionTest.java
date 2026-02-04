package com.revworkforce.test;

import com.revworkforce.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnectionTest {

    public static void main(String[] args) {

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1+2 FROM dual")) {

            if (rs.next()) {
                System.out.println("✅ DB Connection successful. Result = " + rs.getInt(1));
            }

        } catch (Exception e) {
            System.out.println("❌ DB Connection failed");
            e.printStackTrace();
        }
    }
}
