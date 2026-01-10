package commands.usercommands;

import commands.ICommandHandler;
import restaurantproject.model.Message;
import restaurantproject.model.User;
import restaurantproject.model.IUserRepository;

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