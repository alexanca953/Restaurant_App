package model;

import java.util.List;

public interface IUserRepository {
    User login(String username, String password);
    boolean addUser(User user);
    boolean deleteUser(int userId);
    boolean updateUser(int userId, User user);

    List<User> searchUserByName(String name);
    List<User> getAllUsers();

    User getUserByEmail(String email);
}