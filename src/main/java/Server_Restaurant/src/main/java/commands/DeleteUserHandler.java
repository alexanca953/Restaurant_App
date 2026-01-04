package commands;

import model.Message;
import model.repository.UserRepository;

public class DeleteUserHandler implements ICommandHandler {
    private UserRepository userRepo;

    public DeleteUserHandler(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        int userId = (int) data;
        boolean result = userRepo.deleteUser(userId);
        return new Message("DELETE_USER_RESPONSE", result);
    }
}