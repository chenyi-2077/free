package com.freelite.model;

/**
 * 用户实体类
 * A负责
 */
public class User {
    private int id;
    private String email;
    private String password;
    private String role;      // "employer" 或 "freelancer"
    private String displayName;
    private String avatar;
    private String skills;
    private double rating;
    private String createdAt;

    // 构造方法
    public User() {}

    public User(int id, String email, String password, String role, String displayName,
                String avatar, String skills, double rating, String createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.displayName = displayName;
        this.avatar = avatar;
        this.skills = skills;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
