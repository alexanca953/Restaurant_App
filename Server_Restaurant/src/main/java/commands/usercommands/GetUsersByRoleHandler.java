package commands.usercommands;

import commands.ICommandHandler;
import restaurantproject.model.IUserRepository;
import restaurantproject.model.Message;
import restaurantproject.model.User;

import java.util.List;

public class GetUsersByRoleHandler implements ICommandHandler {
    private IUserRepository userRepository;

    public GetUsersByRoleHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message execute(Object data) {
        String role = (String) data;
        System.out.println("SERVER: Cautam useri cu rolul: " + role);
        List<User> users = userRepository.getUsersByRole(role);
        return new Message("GET_USERS_BY_ROLE_RESPONSE", users);
    }
}