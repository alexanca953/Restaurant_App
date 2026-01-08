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
        String sql = "INSERT INTO feedback (client_id, score, comment, type, date_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = repository.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, feedback.getClientId());
            stmt.setInt(2, feedback.getScore());
            stmt.setString(3, feedback.getComment());
            stmt.setString(4, feedback.getType());
            stmt.setTimestamp(5, Timestamp.valueOf(feedback.getDateTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteFeedback(int feedbackId) {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";
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
        String sql = "SELECT * FROM feedback";

        try (Connection conn = repository.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("date_time");
                java.time.LocalDateTime localDateTime = (ts != null) ? ts.toLocalDateTime() : null;

                list.add(new Feedback(
                        rs.getInt("feedback_id"),
                        rs.getInt("client_id"),
                        rs.getInt("score"),
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