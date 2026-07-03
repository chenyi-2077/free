package com.freelite.model;

import java.time.LocalDateTime;

public class Bid {
    private int id;
    private int projectId;
    private int freelancerId;
    private double amount;
    private int days;
    private String proposal;
    private String status;       // "pending" | "accepted" | "rejected"
    private LocalDateTime createdAt;

    // 关联字段
    private String freelancerName;
    private double freelancerRating;

    // --- Getters / Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public int getFreelancerId() { return freelancerId; }
    public void setFreelancerId(int freelancerId) { this.freelancerId = freelancerId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }

    public String getProposal() { return proposal; }
    public void setProposal(String proposal) { this.proposal = proposal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getFreelancerName() { return freelancerName; }
    public void setFreelancerName(String freelancerName) { this.freelancerName = freelancerName; }

    public double getFreelancerRating() { return freelancerRating; }
    public void setFreelancerRating(double freelancerRating) { this.freelancerRating = freelancerRating; }
}
