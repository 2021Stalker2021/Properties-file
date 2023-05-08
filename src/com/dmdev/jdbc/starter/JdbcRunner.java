package com.dmdev.jdbc.starter;

import com.dmdev.jdbc.starter.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {

    public static void main(String[] args) throws SQLException {
//        Long flightId = 2L;
//        var result = getTicketsByFlightId(flightId);
//        System.out.println(result);

//        var result = getFlightsBetween(LocalDate.of(2020, 10, 1).atStartOfDay(), LocalDateTime.now());
//        System.out.println(result);
        try {
            checkMetaData();
        } finally {
            ConnectionManager.ClosePool();
        }
    }

    private static void checkMetaData() throws SQLException {
        try (var connection = ConnectionManager.get()) {
            var metaData = connection.getMetaData();
            var catalogs = metaData.getCatalogs();
            while (catalogs.next()) {
                var catalog = catalogs.getString(1);
                var schemas = metaData.getSchemas();
                while (schemas.next()) {
                    var schema = schemas.getString("TABLE_SCHEM");
                    var tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"});

                    if (schema.equals("public")) {
                        while (tables.next()) {
                            System.out.println(tables.getString("TABLE_NAME"));
                        }
                    }
                }
            }
        }
    }

    private static List<Long> getFlightsBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?
                """;
        List<Long> result = new ArrayList<>();
        try (Connection connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setFetchSize(50);
            prepareStatement.setQueryTimeout(10); // сколько мы максимум будет ждать вып-ие запроса
            prepareStatement.setMaxRows(100); // аналог limit для всех запросов. У FetchSize данные получаются поитерационно , это немного спасает.
            System.out.println(prepareStatement);
            prepareStatement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(prepareStatement);
            prepareStatement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(prepareStatement);

            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class));
            }
            return result;
        }
    }

    public static List<Long> getTicketsByFlightId(Long flightId) throws SQLException {
        String sql = """
                SELECT id 
                from ticket
                WHERE flight_id = ?
                """;
        List<Long> result = new ArrayList<>();
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setLong(1, flightId);

            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getObject("id", Long.class));
            }
            return result;
        }
    }
}
