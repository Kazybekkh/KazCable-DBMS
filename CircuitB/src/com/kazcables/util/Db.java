/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kazcables.util;

// Holds log in info constants for easier access throughout classes

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    public static Connection getConnection() throws SQLException {
        // URL of the database
        String url = "jdbc:mysql://localhost:3306/employees";
        // Database username
        String user = "root";
        // Database password
        String password = "password";
        return DriverManager.getConnection(url, user, password);
    }

}
