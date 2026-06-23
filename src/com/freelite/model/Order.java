package com.freelite.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int projectId;
    private int employerId;
    private int freelancerId;
    private double amount;
    private String status;       // "in_progress" | "completed" | "cancelled"
    private LocalDateTime createdAt;

    // 关联字段
    private String projectTitle;
    private String employerName;
    private String freelancerName;

    // --- Getters / Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public int getEmployerId() { return employerId; }
    public void setEmployerId(int employerId) { this.employerId = employerId; }

    public int getFreelancerId() { return freelancerId; }
    public void setFreelancerId(int freelancerId) { this.freelancerId = freelancerId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }

    public String getFreelancerName() { return freelancerName; }
    public void setFreelancerName(String freelancerName) { this.freelancerName = freelancerName; }
}
