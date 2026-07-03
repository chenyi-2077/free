package chen_zi_hao;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int orderId;
    private int fromUserId;
    private int toUserId;
    private int score;
    private String comment;
    private LocalDateTime createdAt;

    // 关联字段
    private String fromUserName;

    public Review() {}

    // --- getters / setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getFromUserId() { return fromUserId; }
    public void setFromUserId(int fromUserId) { this.fromUserId = fromUserId; }

    public int getToUserId() { return toUserId; }
    public void setToUserId(int toUserId) { this.toUserId = toUserId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getFromUserName() { return fromUserName; }
    public void setFromUserName(String fromUserName) { this.fromUserName = fromUserName; }
}
