package com.crepestrips.userservice.dto;

import java.util.Date;
import java.util.UUID;

public class ReportDTO {
    private UUID reportId;
    private UUID userId;
    private String content;
    private String type;
    private String targetId;
    private Date createdAt;

    public UUID getId() {
        return reportId;
    }
    public void setId(UUID id) {
        this.reportId = id;
    }
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTargetId() {
        return targetId;
    }
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}


