package commands.feedbackcommands;

import commands.ICommandHandler;
import restaurantclient.model.IFeedbackRepository;
import restaurantclient.model.Message;

public class DeleteFeedbackHandler implements ICommandHandler {
    private IFeedbackRepository feedbackRepo;

    public DeleteFeedbackHandler(IFeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public Message execute(Object data) {
        int feedbackId = (int) data;
        boolean result = feedbackRepo.deleteFeedback(feedbackId);
        return new Message("DELETE_FEEDBACK_RESPONSE", result);
    }
}