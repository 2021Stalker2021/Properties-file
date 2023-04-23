package com.dmdev.jdbc.starter;

import com.dmdev.jdbc.starter.util.ConnectionManager;

import java.sql.*;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                UPDATE info 
                SET data = 'TestTest'
                WHERE id = 5
                RETURNING *
                """;
        try (var connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());
            boolean executeResult = statement.execute(sql);
            System.out.println(executeResult);
            System.out.println(statement.getUpdateCount());
        }
    }
}
