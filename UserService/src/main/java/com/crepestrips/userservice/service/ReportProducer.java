package com.crepestrips.userservice.service;

import com.crepestrips.userservice.dto.ReportDTO;
import com.crepestrips.userservice.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReportProducer {

    private final RabbitTemplate rabbitTemplate;

    public ReportProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendReport(ReportDTO dto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_TO_ADMIN_QUEUE, dto);
    }
}