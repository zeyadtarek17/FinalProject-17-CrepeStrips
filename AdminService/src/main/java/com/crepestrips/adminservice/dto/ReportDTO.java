package com.crepestrips.adminservice.dto;

import java.util.Date;
import java.util.UUID;

public class ReportDTO {
    private UUID reportId;
    private UUID userId;
    private String content;
    private String type;
    private String targetId;
    private Date createdAt;

    public ReportDTO() {
    }
    public ReportDTO(UUID reportId, UUID userId, String content, String type, String targetId, Date createdAt) {
        this.reportId = reportId;
        this.userId = userId;
        this.content = content;
        this.type = type;
        this.targetId = targetId;
        this.createdAt = createdAt;
    }
    public ReportDTO(UUID userId, String content, String type, String targetId) {
        this.userId = userId;
        this.content = content;
        this.type = type;
        this.targetId = targetId;
        this.createdAt = new Date();
    }

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


