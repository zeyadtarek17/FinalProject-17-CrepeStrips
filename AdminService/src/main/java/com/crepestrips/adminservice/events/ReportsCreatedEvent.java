package com.crepestrips.adminservice.events;

import java.time.LocalDateTime;

public class ReportsCreatedEvent {
    private String reportId;
    private String restaurantId;
    private String reporterUserId;
    private LocalDateTime reportDate;

    public ReportsCreatedEvent(String reportId, String resutaurantId, String reporterUserId) {
        this.reportId = reportId;
        this.restaurantId = restaurantId;
        this.reporterUserId = reporterUserId;
        this.reportDate = LocalDateTime.now();
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getReporterUserId() {
        return reporterUserId;
    }

    public void setReporterUserId(String reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }
}



