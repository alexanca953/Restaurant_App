package commands.usercommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.UserRepository;
import java.util.List;
import model.User;

public class GetAllUsersHandler implements ICommandHandler {
    private UserRepository userRepo;

    public GetAllUsersHandler(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        List<User> users = userRepo.getAllUsers();
        return new Message("GET_ALL_USERS_RESPONSE", users);
    }
}