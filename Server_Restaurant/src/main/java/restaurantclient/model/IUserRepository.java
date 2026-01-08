package restaurantclient.model;

import java.util.List;

public interface IUserRepository {
    User login(String username, String password);
    boolean addUser(User user);
    boolean deleteUser(int userId);
    boolean updateUser(int userId, User user);

    List<User> searchUserByName(String name);
    List<User> getAllUsers();
    List<User> getUsersByRole(String role);
    User getUserByEmail(String email);
}