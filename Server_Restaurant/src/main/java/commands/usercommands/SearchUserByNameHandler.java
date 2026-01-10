package commands.usercommands;

import commands.ICommandHandler;
import restaurantproject.model.IUserRepository;
import restaurantproject.model.Message;

import java.util.List;
import restaurantproject.model.User;

public class SearchUserByNameHandler implements ICommandHandler {
    private IUserRepository userRepo;

    public SearchUserByNameHandler(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        String name = (String) data;
        List<User> users = userRepo.searchUserByName(name);
        return new Message("SEARCH_USER_RESPONSE", users);
    }
}