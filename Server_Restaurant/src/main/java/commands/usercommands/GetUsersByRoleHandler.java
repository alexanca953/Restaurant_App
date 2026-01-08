package commands.usercommands;

import commands.ICommandHandler;
import restaurantclient.model.IUserRepository;
import restaurantclient.model.Message;
import restaurantclient.model.User;

import java.util.List;

public class GetUsersByRoleHandler implements ICommandHandler {
    private IUserRepository userRepository;

    public GetUsersByRoleHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message execute(Object data) {
        // Data primită trebuie să fie un String (ex: "EMPLOYEE" sau "CLIENT")
        String role = (String) data;

        System.out.println("SERVER: Cautam useri cu rolul: " + role);

        List<User> users = userRepository.getUsersByRole(role);

        return new Message("GET_USERS_BY_ROLE_RESPONSE", users);
    }
}