package com.crepestrips.adminservice.observer;


import com.crepestrips.adminservice.dto.ReportDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class AdminReportHandler implements ReportObserver {

    private final ReportSubject reportSubject;

    public AdminReportHandler(ReportSubject reportSubject) {
        this.reportSubject = reportSubject;
    }

    @PostConstruct
    public void init() {
        reportSubject.addObserver(this);
    }

    @Override
    public void update(ReportDTO report) {
        System.out.println("Admin Report Update" + report.getContent());
    }
}
