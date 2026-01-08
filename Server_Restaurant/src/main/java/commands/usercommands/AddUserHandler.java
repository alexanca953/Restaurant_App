package commands.usercommands;

import commands.ICommandHandler;
import restaurantclient.model.Message;
import restaurantclient.model.User;
import restaurantclient.model.IUserRepository;

public class AddUserHandler implements ICommandHandler {
    private IUserRepository userRepo;

    public AddUserHandler(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        User user = (User) data;
        boolean result = userRepo.addUser(user);
        return new Message("ADD_USER_RESPONSE", result);
    }
}