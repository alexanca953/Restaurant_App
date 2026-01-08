package commands.usercommands;

import commands.ICommandHandler;
import restaurantclient.model.IUserRepository;
import restaurantclient.model.Message;
import restaurantclient.model.User;

public class GetUserByEmailHandler implements ICommandHandler {
    private IUserRepository userRepo;

    public GetUserByEmailHandler(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        String email = (String) data;
        User user = userRepo.getUserByEmail(email);
        return new Message("GET_USER_BY_EMAIL_RESPONSE", user);
    }
}