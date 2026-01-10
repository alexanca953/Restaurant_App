package commands.feedbackcommands;

import commands.ICommandHandler;
import restaurantproject.model.Feedback;
import restaurantproject.model.IFeedbackRepository;
import restaurantproject.model.Message;

public class AddFeedbackHandler implements ICommandHandler {
    private IFeedbackRepository feedbackRepo;

    public AddFeedbackHandler(IFeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public Message execute(Object data) {
        Feedback feedback = (Feedback) data;
        boolean result = feedbackRepo.addFeedback(feedback);
        return new Message("ADD_FEEDBACK_RESPONSE", result);
    }
}