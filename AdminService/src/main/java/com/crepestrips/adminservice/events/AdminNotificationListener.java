//package com.crepestrips.adminservice.events;
//
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AdminNotificationListener implements ReportObserver {
//    private final RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    public AdminNotificationListener(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    @Override
//    public void handleReport(Report report) {
//        // Example: Send a message to RabbitMQ
//        String message = "New report for restaurant: " + report.getRestaurantId();
//        rabbitTemplate.convertAndSend("report-exchange", "report.admin", message);
//    }
//}
