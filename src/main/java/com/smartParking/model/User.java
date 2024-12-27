package com.smartParking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private String licensePlate;
    private BigDecimal totalPenalty;
    private LocalDateTime createdAt;
    private BigDecimal balance;

    public User() {
    }

    // Constructor, Getters, Setters, toString()
    public User(int userId, String username, String password, String role, String email, String licensePlate,BigDecimal totalPenalty , BigDecimal balance, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.licensePlate = licensePlate;
        this.totalPenalty = totalPenalty;
        this.createdAt = createdAt;
        this.balance = balance;
    }

    public User(String username, String password, String role, String email, BigDecimal totalPenalty, String licensePlate , BigDecimal balance) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.licensePlate = licensePlate;
        this.totalPenalty = totalPenalty;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public BigDecimal getTotalPenalty() {
        return totalPenalty;
    }

    public void setTotalPenalty(BigDecimal totalPenalty) {
        this.totalPenalty = totalPenalty;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
