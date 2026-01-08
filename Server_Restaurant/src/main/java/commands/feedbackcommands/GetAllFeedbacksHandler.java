package commands.feedbackcommands;

import commands.ICommandHandler;
import restaurantclient.model.Feedback;
import restaurantclient.model.IFeedbackRepository;
import restaurantclient.model.Message;

import java.util.List;

public class GetAllFeedbacksHandler implements ICommandHandler {
    private IFeedbackRepository feedbackRepo;

    public GetAllFeedbacksHandler(IFeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public Message execute(Object data) {
        List<Feedback> list = feedbackRepo.getAllFeedbacks();
        return new Message("GET_ALL_FEEDBACKS_RESPONSE", list);
    }
}