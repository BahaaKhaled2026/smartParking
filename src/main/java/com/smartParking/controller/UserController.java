package com.smartParking.controller;

import com.smartParking.model.User;
import com.smartParking.service.UserService;
import com.smartParking.service.impl.DashboardServiceImpl;
import com.smartParking.tokenization.JwtResponse;
import com.smartParking.tokenization.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    DashboardServiceImpl dashboardService;



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

    @PostMapping("/signOut")
    public ResponseEntity<?> signOut() {
        userService.signOut();
        return ResponseEntity.ok(Map.of("message", "Signed out successfully"));
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody LoginRequest forgetPasswordRequest) {
        try {
            boolean response = userService.forgetPassword(forgetPasswordRequest.getEmail(), forgetPasswordRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage())); // Return error message if account already exists
        }
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

    @GetMapping("/admin/topUsers")
    public ResponseEntity<Resource> getTopUsers() {
        try{
            dashboardService.topUsersJasperReport();
            // Path to the file
            Path filePath = Paths.get("top_users_report.pdf").toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok().body(resource);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) ;
        }
    }
    @GetMapping("/admin/topLots")
    public ResponseEntity<Resource> getTopLots() {
        try{
            dashboardService.lotsJasperReport();
            // Path to the file
            Path filePath = Paths.get("lots_report.pdf").toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok().body(resource);

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) ;
        }
    }

    @GetMapping("/manager/lots/{managerId}")
    public ResponseEntity<Resource> getLots(@PathVariable ("managerId") int Id) {
        try{
            dashboardService.lotsMangerJasperReport(Id);
            // Path to the file
            Path filePath = Paths.get("lots_manager_report.pdf").toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok().body(resource);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) ;
        }
    }
}
