package com.smartParking.controller;

import com.smartParking.model.User;
import com.smartParking.service.UserService;
import com.smartParking.tokenization.JwtResponse;
import com.smartParking.tokenization.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            Object[] newUser = userService.signup(user);
            return ResponseEntity.ok(new JwtResponse(newUser));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Object[] token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
    }

    @PostMapping("signout")
    public ResponseEntity<?> signout() {
        userService.signOut();
        return ResponseEntity.ok(Map.of("message", "Signed out successfully"));
    }


    @GetMapping("/admin/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/admin/update-role/{userId}")
    public ResponseEntity<String> updateUserRole(@PathVariable int userId, @RequestParam String role) {
        try {
            boolean success = userService.updateUserRole(userId, role);
            return ResponseEntity.ok("User role updated successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
