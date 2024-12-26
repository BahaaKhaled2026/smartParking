package com.smartParking.dao;

import com.smartParking.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    int createUser(User user); // Returns the generated ID
    Optional<User> getUserById(int userId);
    Optional<User> getUserByUsername(String username); // Login
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    boolean updateUserRole(int userId, String newRole);

}
