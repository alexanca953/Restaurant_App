package commands.usercommands;

import commands.ICommandHandler;
import restaurantproject.model.IUserRepository;
import restaurantproject.model.Message;
import restaurantproject.model.User;

public class UpdateUserHandler implements ICommandHandler {
    private IUserRepository userRepo;

    public UpdateUserHandler(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        User userToUpdate = (User)data;
        boolean result = userRepo.updateUser(userToUpdate.getUserId(), userToUpdate);
        return new Message("UPDATE_USER_RESPONSE", result);
    }
}