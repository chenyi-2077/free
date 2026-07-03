package chen_yi_an;

import java.time.LocalDateTime;

public class Delivery {
    private int id;
    private int projectId;
    private int senderId;
    private String content;
    private String fileName;
    private String filePath;
    private LocalDateTime createdAt;

    // joined fields
    private String senderName;

    public Delivery() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    // Aliases for JSP compatibility
    public String getTitle() { return getFileName(); }
    public String getDescription() { return getContent(); }
    public String getUserName() { return getSenderName(); }
}
