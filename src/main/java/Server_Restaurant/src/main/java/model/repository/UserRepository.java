package model.repository;

import model.IUserRepository;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository implements IUserRepository {
    private Repository repository;

    public UserRepository() {
        this.repository = new Repository();
    }

    // Metoda pentru LOGIN
    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Returnam utilizatorul gasit
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("role"),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Returnam null daca nu s-a gasit userul
    }

    // Metoda pentru a adauga un utilizator nou (Register)
    public boolean addUtilizator(Utilizator user) {
        String sql = "INSERT INTO users (role, last_name, first_name, email, password, phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getRol());
            stmt.setString(2, user.getNume());
            stmt.setString(3, user.getPrenume());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getParola());
            stmt.setString(6, user.getTelefon());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
