package commands.usercommands;

import commands.ICommandHandler;
import model.IUserRepository;
import model.Message;

import java.util.List;
import model.User;

public class GetAllUsersHandler implements ICommandHandler {
    private IUserRepository userRepo;

    public GetAllUsersHandler(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        List<User> users = userRepo.getAllUsers();
        return new Message("GET_ALL_USERS_RESPONSE", users);
    }
}