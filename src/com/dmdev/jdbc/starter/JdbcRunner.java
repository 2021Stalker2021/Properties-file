package com.dmdev.jdbc.starter;

import com.dmdev.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        try (var connection = ConnectionManager.open()) {
            System.out.println(connection.getTransactionIsolation());
        }
    }
}
