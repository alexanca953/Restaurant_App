package model.repository;

import model.IReservationRepository;
import model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository implements IReservationRepository {
    private Repository repository;

    public ReservationRepository() {
        this.repository = new Repository();
    }

    @Override
    public boolean addReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (client_id, employee_id, reservation_date, party_size, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservation.getClientId());

            // Verificam daca avem angajat alocat
            if (reservation.getEmployeeId() > 0) {
                stmt.setInt(2, reservation.getEmployeeId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            // CONVERSIE: LocalDateTime -> SQL Timestamp
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getDateTime()));

            // Atentie: In baza de date e 'party_size', in clasa ta e 'numberOfPeople'
            stmt.setInt(4, reservation.getNumberOfPeople());
            stmt.setString(5, reservation.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservation WHERE reservation_id = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateReservation(int reservationId, Reservation reservation) {
        String sql = "UPDATE reservation SET client_id = ?, employee_id = ?, reservation_date = ?, party_size = ?, status = ? WHERE reservation_id = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservation.getClientId());

            if (reservation.getEmployeeId() > 0) {
                stmt.setInt(2, reservation.getEmployeeId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            // CONVERSIE: LocalDateTime -> SQL Timestamp
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getDateTime()));
            stmt.setInt(4, reservation.getNumberOfPeople());
            stmt.setString(5, reservation.getStatus());
            stmt.setInt(6, reservationId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation";

        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // CONVERSIE: SQL Timestamp -> LocalDateTime
                Timestamp ts = rs.getTimestamp("reservation_date");
                java.time.LocalDateTime localDateTime = (ts != null) ? ts.toLocalDateTime() : null;

                list.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("client_id"),
                        rs.getInt("employee_id"),
                        localDateTime, // Aici punem variabila convertita
                        rs.getInt("party_size"), // mapam coloana party_size la numberOfPeople
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Reservation> getReservationsByClient(int clientId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE client_id = ?";

        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("reservation_date");
                java.time.LocalDateTime localDateTime = (ts != null) ? ts.toLocalDateTime() : null;

                list.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("client_id"),
                        rs.getInt("employee_id"),
                        localDateTime,
                        rs.getInt("party_size"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}