package com.freelite.model;

import java.time.LocalDateTime;

public class TransactionLog {
    private int id;
    private int userId;
    private String type;       // recharge/freeze/release/refund/withdraw
    private double amount;
    private double balanceBefore;
    private double balanceAfter;
    private double frozenBefore;
    private double frozenAfter;
    private Integer orderId;
    private String description;
    private LocalDateTime createdAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getBalanceBefore() { return balanceBefore; }
    public void setBalanceBefore(double balanceBefore) { this.balanceBefore = balanceBefore; }
    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
    public double getFrozenBefore() { return frozenBefore; }
    public void setFrozenBefore(double frozenBefore) { this.frozenBefore = frozenBefore; }
    public double getFrozenAfter() { return frozenAfter; }
    public void setFrozenAfter(double frozenAfter) { this.frozenAfter = frozenAfter; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
