package commands.usercommands;

import commands.ICommandHandler;
import model.Message;
import model.User;
import model.repository.UserRepository;

public class UpdateUserHandler implements ICommandHandler {
    private UserRepository userRepo;

    public UpdateUserHandler(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        User userToUpdate = (User) data;
        boolean result = userRepo.updateUser(userToUpdate.getUserId(), userToUpdate);
        return new Message("UPDATE_USER_RESPONSE", result);
    }
}