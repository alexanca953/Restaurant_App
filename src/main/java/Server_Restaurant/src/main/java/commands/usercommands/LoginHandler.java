package commands.usercommands;

import commands.ICommandHandler;
import model.IUserRepository;
import model.Message;
import model.User;
import model.repository.UserRepository;

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