package commands.feedbackcommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.FeedbackRepository;

public class DeleteFeedbackHandler implements ICommandHandler {
    private FeedbackRepository feedbackRepo;

    public DeleteFeedbackHandler(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public Message execute(Object data) {
        int feedbackId = (int) data;
        boolean result = feedbackRepo.deleteFeedback(feedbackId);
        return new Message("DELETE_FEEDBACK_RESPONSE", result);
    }
}