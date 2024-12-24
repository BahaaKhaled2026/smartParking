package com.smartParking.service.impl;

import com.smartParking.dao.UserDAO;
import com.smartParking.model.User;
import com.smartParking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int signup(User user) {
        userDAO.getUserByUsername(user.getUsername()).ifPresent(existing -> {
            throw new IllegalStateException("Username already exists.");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDAO.createUser(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Invalid username or password."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalStateException("Invalid username or password.");
        }
        return user;
    }

    @Override
    public User getUserById(int userId) {
        return userDAO.getUserById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public boolean updateUserRole(int userId, String newRole) {
        if (!List.of("DRIVER", "MANAGER", "ADMIN").contains(newRole)) {
            throw new IllegalStateException("Invalid role: " + newRole);
        }
        return userDAO.updateUserRole(userId, newRole);
    }
}

