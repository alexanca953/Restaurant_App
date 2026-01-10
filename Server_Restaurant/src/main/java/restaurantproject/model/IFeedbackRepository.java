package restaurantproject.model;

import java.util.List;

public interface IFeedbackRepository {
    boolean addFeedback(Feedback feedback);
    boolean deleteFeedback(int feedbackId);
    List<Feedback> getAllFeedbacks();
}