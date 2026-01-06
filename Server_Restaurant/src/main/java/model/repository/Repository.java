package model.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Repository {
    Connection SqlConnection;
    public Repository() {
        try {
            String url = "jdbc:mysql://localhost:3306/restaurant_db";
            String username = "root";
            String password = "root";
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.SqlConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Conexiune reusita la baza de date!");
        } catch (ClassNotFoundException e) {
            System.out.println("Nu am gasit Driverul de MySQL! Verifica pom.xml.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Eroare la conectare SQL (Verifica user/parola/nume baza)!");
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return this.SqlConnection;
    }
}
