package model.repository;

import model.Feedback;
import model.IFeedbackRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository implements IFeedbackRepository {
    private Repository repository;

    public FeedbackRepository() {
        this.repository = new Repository();
    }

    @Override
    public boolean addFeedback(Feedback feedback) {
        String sql = "INSERT INTO review (client_id, employee_id, product_id, rating, comment, type, date_posted) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, feedback.getClientId());

            if (feedback.getEmployeeId() > 0) {
                stmt.setInt(2, feedback.getEmployeeId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            if (feedback.getProductId() > 0) {
                stmt.setInt(3, feedback.getProductId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setInt(4, feedback.getScore());
            stmt.setString(5, feedback.getComment());
            stmt.setString(6, feedback.getType());
            stmt.setTimestamp(7, Timestamp.valueOf(feedback.getDateTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteFeedback(int feedbackId) {
        String sql = "DELETE FROM review WHERE review_id = ?";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, feedbackId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM review";

        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("date_posted");
                java.time.LocalDateTime localDateTime = (ts != null) ? ts.toLocalDateTime() : null;

                list.add(new Feedback(
                        rs.getInt("review_id"),
                        rs.getInt("client_id"),
                        rs.getInt("employee_id"),
                        rs.getInt("product_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getString("type"),
                        localDateTime
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}