package com.crepestrips.adminservice.observer;


import com.crepestrips.adminservice.config.RabbitMQConfig;
import com.crepestrips.adminservice.dto.ReportDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReportListener {

    private final ReportSubject reportSubject;

    public ReportListener(ReportSubject reportSubject) {
        this.reportSubject = reportSubject;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_TO_ADMIN_QUEUE)
    public void receiveReport(ReportDTO report) {
        reportSubject.notifyObservers(report);
    }
}
