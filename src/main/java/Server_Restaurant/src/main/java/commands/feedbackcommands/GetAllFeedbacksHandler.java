package commands.feedbackcommands;

import commands.ICommandHandler;
import model.Feedback;
import model.Message;
import model.repository.FeedbackRepository;
import java.util.List;

public class GetAllFeedbacksHandler implements ICommandHandler {
    private FeedbackRepository feedbackRepo;

    public GetAllFeedbacksHandler(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Feedback> list = feedbackRepo.getAllFeedbacks();
        return new Message("GET_ALL_FEEDBACKS_RESPONSE", list);
    }
}