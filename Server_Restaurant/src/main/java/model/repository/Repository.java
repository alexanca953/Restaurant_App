package model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Repository {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Repository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("DID NOT FIND DRIVER MYSQL");
            e.printStackTrace();
        }
    }
    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        return conn;
    }
}