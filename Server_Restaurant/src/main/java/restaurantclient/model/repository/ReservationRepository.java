package restaurantclient.model.repository;

import restaurantclient.model.IReservationRepository;
import restaurantclient.model.Reservation;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationRepository implements IReservationRepository {
    private Repository repository;

    public ReservationRepository() {
        this.repository =new Repository();
    }
    @Override
    public boolean addReservation(Reservation reservation) {
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
            conn.setAutoCommit(false);
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
            int finalUserId = reservation.getClientId();
            if (finalUserId == 0 && reservation.getTempClientPhone() != null) {
                String findUserSql = "SELECT user_id FROM user WHERE phone = ?";
                try (PreparedStatement s = conn.prepareStatement(findUserSql)) {
                    s.setString(1, reservation.getTempClientPhone());
                    ResultSet rs = s.executeQuery();
                    if (rs.next()) {
                        finalUserId = rs.getInt("user_id");
                    }
                }
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
            if (finalUserId == 0) {
                System.out.println("EROARE: Nu s-a putut asocia un User.");
                conn.rollback();
                return false;
            }
            String sqlRes = "INSERT INTO reservation (user_id, reservation_date, party_size, status) VALUES (?, ?, ?, ?)";

            int newReservationId = 0;
            try (PreparedStatement stmtRes = conn.prepareStatement(sqlRes, Statement.RETURN_GENERATED_KEYS)) {
                stmtRes.setInt(1, finalUserId);
                stmtRes.setTimestamp(2, Timestamp.valueOf(reservation.getDateTime()));
                stmtRes.setInt(3, reservation.getNumberOfPeople());
                stmtRes.setString(4, reservation.getStatus());
                if (stmtRes.executeUpdate() == 0) {
                    conn.rollback(); return false;
                }
                ResultSet generatedKeys = stmtRes.getGeneratedKeys();
                if (generatedKeys.next()) newReservationId = generatedKeys.getInt(1);
                else { conn.rollback(); return false; }
            }
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
    @Override
    public boolean deleteReservation(int id) {
        Connection conn = null;
        try {
            conn = repository.getConnection();
            conn.setAutoCommit(false);
            String sqlLink = "DELETE FROM reservation_table WHERE reservation_id = ?";
            try (PreparedStatement stmtLink = conn.prepareStatement(sqlLink)) {
                stmtLink.setInt(1, id);
                stmtLink.executeUpdate();
            }
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

    @Override
    public boolean updateReservation(Reservation reservation) {
        if (reservation.getDateTime() == null) return false;

        Connection conn = null;
        try {
            conn = repository.getConnection();
            conn.setAutoCommit(false);
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
                            return false;
                        }
                    }
                }
            }

            if (reservation.getTableId() > 0) {
                String checkSql = "SELECT COUNT(*) FROM reservation r " +
                        "JOIN reservation_table rt ON r.reservation_id = rt.reservation_id " +
                        "WHERE rt.table_id = ? " +
                        "AND r.reservation_id != ? " +
                        "AND r.status != 'CANCELLED' " +
                        "AND r.reservation_date < ? " +
                        "AND DATE_ADD(r.reservation_date, INTERVAL 2 HOUR) > ?";
                try (PreparedStatement s = conn.prepareStatement(checkSql)) {
                    s.setInt(1, reservation.getTableId());
                    s.setInt(2, reservation.getReservationId());
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

            String delLinkSql = "DELETE FROM reservation_table WHERE reservation_id = ?";
            try (PreparedStatement s = conn.prepareStatement(delLinkSql)) {
                s.setInt(1, reservation.getReservationId());
                s.executeUpdate();
            }
            if (reservation.getTableId() > 0) {
                String addLinkSql = "INSERT INTO reservation_table (reservation_id, table_id) VALUES (?, ?)";
                try (PreparedStatement s = conn.prepareStatement(addLinkSql)) {
                    s.setInt(1, reservation.getReservationId());
                    s.setInt(2, reservation.getTableId());
                    s.executeUpdate();
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

    public List<Map<String, Object>> getAllReservationsAsMap() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT r.*, " +
                "CONCAT(u.first_name, ' ', u.last_name) AS full_name, " +
                "u.phone, " +
                "rt.table_id " +
                "FROM reservation r " +
                "LEFT JOIN user u ON r.user_id = u.user_id " +
                "LEFT JOIN reservation_table rt ON r.reservation_id = rt.reservation_id";

        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("reservationId", rs.getInt("reservation_id"));
                row.put("userId", rs.getInt("user_id"));
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
    @Override
    public List<Reservation> getAllReservations() { return new ArrayList<>(); }
    @Override
    public List<Reservation> getReservationsByClient(int clientId) {
        List<Reservation> list = new ArrayList<>();

        String sql = "SELECT r.*, rt.table_id " +
                "FROM reservation r " +
                "LEFT JOIN reservation_table rt ON r.reservation_id = rt.reservation_id " +
                "WHERE r.user_id = ?";

        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation res = new Reservation();

                    res.setReservationId(rs.getInt("reservation_id"));
                    res.setClientId(rs.getInt("user_id"));
                    res.setNumberOfPeople(rs.getInt("party_size"));
                    res.setStatus(rs.getString("status"));

                    Timestamp ts = rs.getTimestamp("reservation_date");
                    if (ts != null) {
                        res.setDateTime(ts.toLocalDateTime());
                    }
                    int tId = rs.getInt("table_id");
                    res.setTableId(rs.wasNull() ? 0 : tId);

                    list.add(res);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}