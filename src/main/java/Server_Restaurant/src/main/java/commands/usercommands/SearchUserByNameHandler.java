package commands.usercommands;

import commands.ICommandHandler;
import model.Message;
import model.repository.UserRepository;
import java.util.List;
import model.User;

public class SearchUserByNameHandler implements ICommandHandler {
    private UserRepository userRepo;

    public SearchUserByNameHandler(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        String name = (String) data;
        List<User> users = userRepo.searchUserByName(name);
        return new Message("SEARCH_USER_RESPONSE", users);
    }
}