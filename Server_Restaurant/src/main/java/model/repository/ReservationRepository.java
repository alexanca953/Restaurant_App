package model.repository;

import model.IReservationRepository;
import model.Reservation;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationRepository implements IReservationRepository {
    // Folosim RestaurantConnection (Singleton-ul tău)
    private Repository repository;

    public ReservationRepository() {
        this.repository =new Repository();
    }

    // =========================================================================
    // 1. ADD RESERVATION (Logica Nouă: User direct)
    // =========================================================================
    @Override
    public boolean addReservation(Reservation reservation) {
        // Validări simple
        if (reservation.getDateTime() == null) {
            System.out.println("EROARE: Data e NULL.");
            return false;
        }
        if (reservation.getDateTime().isBefore(LocalDateTime.now())) {
            System.out.println("EROARE: Nu poți face rezervări în trecut!");
            return false;
        }

        Connection conn = null;
        try {
            conn = repository.getConnection();
            conn.setAutoCommit(false); // START TRANZACȚIE

            // -----------------------------------------------------------------
            // A. VERIFICARE CONFLICT MASĂ
            // -----------------------------------------------------------------
            if (reservation.getTableId() > 0) {
                String checkSql = "SELECT COUNT(*) FROM reservation r " +
                        "JOIN reservation_table rt ON r.reservation_id = rt.reservation_id " +
                        "WHERE rt.table_id = ? AND r.status != 'CANCELLED' " +
                        "AND r.reservation_date < ? " +
                        "AND DATE_ADD(r.reservation_date, INTERVAL 2 HOUR) > ?";

                try (PreparedStatement stmtCheck = conn.prepareStatement(checkSql)) {
                    stmtCheck.setInt(1, reservation.getTableId());
                    stmtCheck.setTimestamp(2, Timestamp.valueOf(reservation.getDateTime().plusHours(2)));
                    stmtCheck.setTimestamp(3, Timestamp.valueOf(reservation.getDateTime()));

                    ResultSet rs = stmtCheck.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("CONFLICT MASA");
                        conn.rollback();
                        return false;
                    }
                }
            }

            // -----------------------------------------------------------------
            // B. GESTIONARE USER (Căutare sau Creare User Nou)
            // -----------------------------------------------------------------
            int finalUserId = reservation.getClientId(); // Folosim userId, nu clientId

            // Dacă e "Walk-in" (nu are ID setat, dar are telefon)
            if (finalUserId == 0 && reservation.getTempClientPhone() != null) {

                // 1. Căutăm User după telefon
                String findUserSql = "SELECT user_id FROM user WHERE phone = ?";
                try (PreparedStatement s = conn.prepareStatement(findUserSql)) {
                    s.setString(1, reservation.getTempClientPhone());
                    ResultSet rs = s.executeQuery();
                    if (rs.next()) {
                        finalUserId = rs.getInt("user_id");
                    }
                }

                // 2. Dacă nu există, CREĂM USER NOU (Rol = CLIENT)
                if (finalUserId == 0) {
                    String fullName = reservation.getTempClientName();
                    String firstName = fullName;
                    String lastName = "";
                    if (fullName != null && fullName.contains(" ")) {
                        String[] parts = fullName.split(" ", 2);
                        firstName = parts[0];
                        lastName = parts[1];
                    } else if (fullName == null) {
                        firstName = "Client";
                        lastName = "Nou";
                    }

                    // Generăm un email dummy unic ca să nu crape baza de date (UNIQUE constraint)
                    String dummyEmail = "no_email_" + System.currentTimeMillis() + "@local.com";

                    String createUserSql = "INSERT INTO user (first_name, last_name, phone, password, email, role) VALUES (?, ?, ?, 'no_pass', ?, 'CLIENT')";
                    try (PreparedStatement s2 = conn.prepareStatement(createUserSql, Statement.RETURN_GENERATED_KEYS)) {
                        s2.setString(1, firstName);
                        s2.setString(2, lastName);
                        s2.setString(3, reservation.getTempClientPhone());
                        s2.setString(4, dummyEmail);

                        s2.executeUpdate();
                        ResultSet gk = s2.getGeneratedKeys();
                        if (gk.next()) finalUserId = gk.getInt(1);
                    }
                }
            }

            // Dacă tot nu am găsit un user valid, oprim totul
            if (finalUserId == 0) {
                System.out.println("EROARE: Nu s-a putut asocia un User.");
                conn.rollback();
                return false;
            }

            // -----------------------------------------------------------------
            // C. INSERARE REZERVARE (Folosind user_id)
            // -----------------------------------------------------------------
            String sqlRes = "INSERT INTO reservation (user_id, employee_id, reservation_date, party_size, status) VALUES (?, ?, ?, ?, ?)";
            int newReservationId = 0;

            try (PreparedStatement stmtRes = conn.prepareStatement(sqlRes, Statement.RETURN_GENERATED_KEYS)) {
                stmtRes.setInt(1, finalUserId); // Punem USER ID

                if (reservation.getEmployeeId() > 0) stmtRes.setInt(2, reservation.getEmployeeId());
                else stmtRes.setNull(2, Types.INTEGER);

                stmtRes.setTimestamp(3, Timestamp.valueOf(reservation.getDateTime()));
                stmtRes.setInt(4, reservation.getNumberOfPeople());
                stmtRes.setString(5, reservation.getStatus());

                if (stmtRes.executeUpdate() == 0) {
                    conn.rollback(); return false;
                }

                ResultSet generatedKeys = stmtRes.getGeneratedKeys();
                if (generatedKeys.next()) newReservationId = generatedKeys.getInt(1);
                else { conn.rollback(); return false; }
            }

            // -----------------------------------------------------------------
            // D. INSERARE LEGĂTURĂ MASĂ
            // -----------------------------------------------------------------
            if (reservation.getTableId() > 0) {
                String sqlLink = "INSERT INTO reservation_table (reservation_id, table_id) VALUES (?, ?)";
                try (PreparedStatement stmtLink = conn.prepareStatement(sqlLink)) {
                    stmtLink.setInt(1, newReservationId);
                    stmtLink.setInt(2, reservation.getTableId());
                    stmtLink.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception e) {}
        }
    }

    // =========================================================================
    // 2. DELETE RESERVATION (Curățare)
    // =========================================================================
    @Override
    public boolean deleteReservation(int id) {
        Connection conn = null;
        try {
            conn = repository.getConnection();
            conn.setAutoCommit(false);

            // Ștergem link-ul cu masa
            String sqlLink = "DELETE FROM reservation_table WHERE reservation_id = ?";
            try (PreparedStatement stmtLink = conn.prepareStatement(sqlLink)) {
                stmtLink.setInt(1, id);
                stmtLink.executeUpdate();
            }

            // Ștergem rezervarea
            String sqlRes = "DELETE FROM reservation WHERE reservation_id = ?";
            try (PreparedStatement stmtRes = conn.prepareStatement(sqlRes)) {
                stmtRes.setInt(1, id);
                int rows = stmtRes.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception e) {}
        }
    }

    // =========================================================================
    // 3. UPDATE RESERVATION (Update direct pe User și Reservation)
    // =========================================================================
    @Override
    public boolean updateReservation(Reservation reservation) {
        if (reservation.getDateTime() == null) return false;

        Connection conn = null;
        try {
            conn = repository.getConnection();
            conn.setAutoCommit(false); // START TRANZACȚIE

            // =================================================================
            // 1. VERIFICARE CAPACITATE (Încăpem la masă?)
            // =================================================================
            if (reservation.getTableId() > 0) {
                String capSql = "SELECT capacity FROM restaurant_table WHERE table_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(capSql)) {
                    ps.setInt(1, reservation.getTableId());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int capacity = rs.getInt("capacity");
                        if (reservation.getNumberOfPeople() > capacity) {
                            System.out.println("EROARE EDIT: Masa e prea mică! (Cap: " + capacity + ", Pers: " + reservation.getNumberOfPeople() + ")");
                            conn.rollback();
                            return false; // Respingem modificarea
                        }
                    }
                }
            }

            // =================================================================
            // 2. VERIFICARE DISPONIBILITATE (E liberă masa la ora aia?)
            // =================================================================
            if (reservation.getTableId() > 0) {
                // IMPORTANT: "AND r.reservation_id != ?" -> Ignorăm rezervarea curentă ca să nu dea conflict cu ea însăși
                String checkSql = "SELECT COUNT(*) FROM reservation r " +
                        "JOIN reservation_table rt ON r.reservation_id = rt.reservation_id " +
                        "WHERE rt.table_id = ? " +
                        "AND r.reservation_id != ? " +
                        "AND r.status != 'CANCELLED' " +
                        "AND r.reservation_date < ? " +
                        "AND DATE_ADD(r.reservation_date, INTERVAL 2 HOUR) > ?";

                try (PreparedStatement s = conn.prepareStatement(checkSql)) {
                    s.setInt(1, reservation.getTableId());
                    s.setInt(2, reservation.getReservationId()); // ID-ul nostru (exclus din căutare)
                    s.setTimestamp(3, Timestamp.valueOf(reservation.getDateTime().plusHours(2)));
                    s.setTimestamp(4, Timestamp.valueOf(reservation.getDateTime()));

                    ResultSet rs = s.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("EROARE EDIT: Masa e ocupată de altcineva în acest interval.");
                        conn.rollback();
                        return false;
                    }
                }
            }

            // =================================================================
            // 3. ACTUALIZARE DATE USER (Nume/Telefon)
            // =================================================================
            if (reservation.getTempClientName() != null && reservation.getClientId() > 0) {
                String fullName = reservation.getTempClientName();
                String fName = fullName.split(" ")[0];
                String lName = fullName.contains(" ") ? fullName.substring(fullName.indexOf(" ") + 1) : "";

                String updateUserSql = "UPDATE user SET first_name = ?, last_name = ?, phone = ? WHERE user_id = ?";
                try (PreparedStatement s = conn.prepareStatement(updateUserSql)) {
                    s.setString(1, fName);
                    s.setString(2, lName);
                    s.setString(3, reservation.getTempClientPhone());
                    s.setInt(4, reservation.getClientId());
                    s.executeUpdate();
                }
            }

            // =================================================================
            // 4. ACTUALIZARE REZERVARE
            // =================================================================
            String updateResSql = "UPDATE reservation SET reservation_date = ?, party_size = ?, status = ? WHERE reservation_id = ?";
            try (PreparedStatement s = conn.prepareStatement(updateResSql)) {
                s.setTimestamp(1, Timestamp.valueOf(reservation.getDateTime()));
                s.setInt(2, reservation.getNumberOfPeople());
                s.setString(3, reservation.getStatus());
                s.setInt(4, reservation.getReservationId());

                int affected = s.executeUpdate();
                if (affected == 0) {
                    conn.rollback(); return false;
                }
            }

            // =================================================================
            // 5. ACTUALIZARE LEGĂTURĂ MASĂ
            // =================================================================
            // Ștergem legătura veche
            String delLinkSql = "DELETE FROM reservation_table WHERE reservation_id = ?";
            try (PreparedStatement s = conn.prepareStatement(delLinkSql)) {
                s.setInt(1, reservation.getReservationId());
                s.executeUpdate();
            }

            // Adăugăm legătura nouă (dacă s-a ales o masă)
            if (reservation.getTableId() > 0) {
                String addLinkSql = "INSERT INTO reservation_table (reservation_id, table_id) VALUES (?, ?)";
                try (PreparedStatement s = conn.prepareStatement(addLinkSql)) {
                    s.setInt(1, reservation.getReservationId());
                    s.setInt(2, reservation.getTableId());
                    s.executeUpdate();
                }
            }

            conn.commit(); // SALVARE
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (Exception e) {}
        }
    }

    // =========================================================================
    // 4. GET ALL AS MAP (Afișare)
    // =========================================================================
    public List<Map<String, Object>> getAllReservationsAsMap() {
        List<Map<String, Object>> list = new ArrayList<>();

        // SQL SIMPLIFICAT: Legătură directă reservation -> user (Fără Client)
        String sql = "SELECT r.*, " +
                "CONCAT(u.first_name, ' ', u.last_name) AS full_name, " +
                "u.phone, " +
                "rt.table_id " +
                "FROM reservation r " +
                "LEFT JOIN user u ON r.user_id = u.user_id " + // Join User
                "LEFT JOIN reservation_table rt ON r.reservation_id = rt.reservation_id";

        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("reservationId", rs.getInt("reservation_id"));
                row.put("userId", rs.getInt("user_id")); // user_id
                row.put("numberOfPeople", rs.getInt("party_size"));
                row.put("status", rs.getString("status"));

                Timestamp ts = rs.getTimestamp("reservation_date");
                row.put("reservationDate", (ts != null) ? ts.toLocalDateTime() : null);

                int tId = rs.getInt("table_id");
                row.put("tableId", rs.wasNull() ? 0 : tId);

                String name = rs.getString("full_name");
                row.put("customerName", (name != null) ? name : "Unknown");

                String phone = rs.getString("phone");
                row.put("phoneNumber", (phone != null) ? phone : "No Phone");

                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Metode din interfață (nefolosite momentan, dar necesare pt compilare)
    @Override
    public List<Reservation> getAllReservations() { return new ArrayList<>(); }

    @Override
    public List<Reservation> getReservationsByClient(int clientId) {
        return List.of();
    }
}