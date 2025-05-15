package com.crepestrips.adminservice.service;

import com.crepestrips.adminservice.config.RabbitMQConfig;
import com.crepestrips.adminservice.dto.ReportDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReportConsumer {

    @RabbitListener(queues = RabbitMQConfig.USER_TO_ADMIN_QUEUE)
    public void receiveReport(ReportDTO dto) {
        if ("restaurant".equalsIgnoreCase(dto.getType())) {
            System.out.println("📣 Admin notified of a reported restaurant!");
            System.out.println("📝 Content: " + dto.getContent());
            System.out.println("🧾 User ID: " + dto.getUserId());
            System.out.println("🏢 Target Restaurant ID: " + dto.getTargetId());
            System.out.println("📅 Created At: " + dto.getCreatedAt());
        }
    }
}
