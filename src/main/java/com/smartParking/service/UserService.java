package com.smartParking.service;

import com.smartParking.model.User;
import java.util.List;

public interface UserService {
    Object[] signup(User user); // Signup a new user
    Object[] login(String username, String password); // Authenticate a user
    User getUserById(int userId); // Retrieve user details
    List<User> getAllUsers(); // Admin-only: Retrieve all users
    boolean updateUserRole(int userId, String newRole); // Admin-only: Update user roles

    public void signOut(); // Sign out a user
}
