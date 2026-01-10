package commands.usercommands;

import commands.ICommandHandler;
import restaurantproject.model.IUserRepository;
import restaurantproject.model.Message;

public class DeleteUserHandler implements ICommandHandler {
    private IUserRepository userRepo;

    public DeleteUserHandler(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        int userId = (int) data;
        boolean result = userRepo.deleteUser(userId);
        return new Message("DELETE_USER_RESPONSE", result);
    }
}