package com.crepestrips.adminservice.observer;

import com.crepestrips.adminservice.dto.ReportDTO;
import com.crepestrips.adminservice.model.Admin;
import com.crepestrips.adminservice.repository.AdminRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailReportObserver implements ReportObserver {

    private final AdminRepository adminRepo;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailReportObserver(AdminRepository adminRepo, JavaMailSender mailSender) {
        this.adminRepo = adminRepo;
        this.mailSender = mailSender;
    }

    @Override
    public void update(ReportDTO dto) {
        try {
            List<Admin> admins = adminRepo.findAll();
            if (admins == null || admins.isEmpty()) {
                System.err.println("No admins found.");
                return;
            }

            String[] adminEmails = admins.stream()
                    .map(Admin::getEmail)
                    .filter(email -> email != null && !email.isEmpty())
                    .toArray(String[]::new);

            if (adminEmails.length == 0) {
                System.err.println("No admin emails available.");
                return;
            }

            String subject = "New Report Received";
            String body = "A new report has been received from User ID: " + dto.getUserId() + "\n" +
                    "Report Type: " + dto.getType() + "\n" +
                    "Report Content: " + dto.getContent() + "\n" +
                    "Target ID: " + dto.getTargetId() + "\n" +
                    "Created At: " + dto.getCreatedAt();

            sendMail(subject, body, adminEmails);

        } catch (Exception e) {
            System.err.println("Failed to process report notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendMail(String subject, String body, String... to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(subject);
            message.setText(body);
            message.setTo(to);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
