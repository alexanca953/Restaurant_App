package commands.usercommands;

import commands.ICommandHandler;
import restaurantproject.model.IUserRepository;
import restaurantproject.model.Message;
import restaurantproject.model.User;

public class LoginHandler implements ICommandHandler {
    private IUserRepository userRepo;

    public LoginHandler(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Message execute(Object data) {
        User loginData = (User) data;
        User user = userRepo.login(loginData.getEmail(), loginData.getPassword());

        if (user != null) {
            return new Message("LOGIN_RESPONSE", user);
        } else {
            return new Message("LOGIN_RESPONSE", null);
        }
    }
}