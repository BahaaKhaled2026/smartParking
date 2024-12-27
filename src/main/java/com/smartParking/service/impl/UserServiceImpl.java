package com.smartParking.service.impl;

import com.smartParking.WebSocketNotificationService;
import com.smartParking.dao.UserDAO;
import com.smartParking.model.User;
import com.smartParking.service.UserService;
import com.smartParking.tokenization.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    private final JwtUtils jwtUtils = new JwtUtils();


    @Override
    public Object[] signup(User user) {
        userDAO.getUserByUsername(user.getUsername()).ifPresent(existing -> {
            throw new IllegalStateException("Username already exists.");
        });

        userDAO.getUserByEmail(user.getEmail()).ifPresent(existing -> {
            throw new IllegalStateException("Email already exists.");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.createUser(user);
        return new Object[] {user, jwtUtils.generateToken(user.getEmail())};
    }

    @Override
    public Object[] login(String email, String password) {
        User user = userDAO.getUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Invalid username or password."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalStateException("Invalid username or password.");
        }
        webSocketNotificationService.testNotify("User " + user.getUsername() + " logged in.");
        return new Object[] {user, jwtUtils.generateToken(email)};
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

    @Override
    public void signOut() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        SecurityContextHolder.clearContext();
    }

    @Override
    public boolean forgetPassword(String email, String newPassword) {
        User user = userDAO.getUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found with account: " + email));
        user.setPassword(passwordEncoder.encode(newPassword));
        userDAO.updateUser(user);
        return true;
    }
}

