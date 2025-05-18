package com.crepestrips.adminservice.service;

import com.crepestrips.adminservice.RabbitMQ.RabbitMQConfig;
import com.crepestrips.adminservice.dto.ReportDTO;
import com.crepestrips.adminservice.model.Admin;
import com.crepestrips.adminservice.observer.EmailReportObserver;
import com.crepestrips.adminservice.observer.ReportObserver;
import com.crepestrips.adminservice.observer.ReportSubject;
import com.crepestrips.adminservice.repository.AdminRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final ReportSubject reportSubject;
    private final EmailReportObserver emailReportObserver;

    @Autowired
    public AdminService(AdminRepository adminRepo, PasswordEncoder passwordEncoder, JavaMailSender mailSender, ReportSubject reportSubject, EmailReportObserver emailReportObserver) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.reportSubject = reportSubject;
        this.emailReportObserver = emailReportObserver;
    }

    public Admin createAdmin(Admin admin) {

        if (adminRepo.findByUsername(admin.getUsername()) != null) {
            throw new RuntimeException("Admin already exists");
        }
        else {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setEmail(admin.getUsername() + "@gmail.com");
            return adminRepo.save(admin);
        }
    }

    public void login(String username) {
        Admin user = adminRepo.findByUsername(username);
        user.setLoggedIn(true);
        adminRepo.save(user);
    }

    public void logout(String userId) {
        // get user by id
        Admin user = adminRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setLoggedIn(false);
        adminRepo.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepo.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(
                admin.getUsername(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) // Or map user roles/authorities here
        );
    }
//    @RabbitListener(queues = RabbitMQConfig.USER_TO_ADMIN_QUEUE, containerFactory = "rabbitListenerContainerFactory")
//    public void getAllReports(ReportDTO dto) {
//        if (dto == null) {
//            System.err.println("Received null ReportDTO");
//            return;
//        }
//        if (adminRepo == null) {
//            System.err.println("adminRepo is null!");
//            return;
//        }
//        List<Admin> admins = adminRepo.findAll();
//        if (admins == null || admins.isEmpty()) {
//            System.err.println("No admins found");
//            return;
//        }
//        String[] adminEmails = admins.stream()
//                .map(Admin::getEmail)
//                .filter(email -> email != null && !email.isEmpty())
//                .toArray(String[]::new);
//
//        if (adminEmails.length == 0) {
//            System.err.println("No admin emails available");
//            return;
//        }
//
//        String subject = "New Report Received";
//        String body = "A new report has been received from User ID: " + dto.getUserId() + "\n" +
//                "Report Type: " + dto.getType() + "\n" +
//                "Report Content: " + dto.getContent() + "\n" +
//                "Target ID: " + dto.getTargetId() + "\n" +
//                "Created At: " + dto.getCreatedAt();
//
//        try {
//            sendMail(subject, body, adminEmails);
//        } catch (Exception e) {
//            System.err.println("Failed to send email: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    //Create a method to get all reports from user service by consuming
////    @RabbitListener(queues = RabbitMQConfig.USER_TO_ADMIN_QUEUE)
////    @RabbitListener(queues = RabbitMQConfig.USER_TO_ADMIN_QUEUE, containerFactory = "rabbitListenerContainerFactory")
////    public void getAllReports(ReportDTO dto) {
////
////        //get all admins emails
////        List<Admin> admins = adminRepo.findAll();
////        String[] adminEmails = admins.stream()
////                .map(Admin::getEmail)
////                .toArray(String[]::new);
////        //send email to all admins
////        String subject = "New Report Received";
////        String body = "A new report has been received from" +  "User ID: " + dto.getUserId() + "\n"+
////                "Report Type: " + dto.getType() + "\n" +
////                "Report Content: " + dto.getContent() + "\n" +
////                "Target ID: " + dto.getTargetId() + "\n" +
////                "Created At: " + dto.getCreatedAt();
////        sendMail(subject, body, adminEmails);
////
////    }
//
//    //    public void receiveReport(ReportDTO dto) {
//    //        if ("restaurant".equalsIgnoreCase(dto.getType())) {
//    //            System.out.println("📣 Admin notified of a reported restaurant!");
//    //            System.out.println("📝 Content: " + dto.getContent());
//    //            System.out.println("🧾 User ID: " + dto.getUserId());
//    //            System.out.println("🏢 Target Restaurant ID: " + dto.getTargetId());
//    //            System.out.println("📅 Created At: " + dto.getCreatedAt());
//    //        }
//    //    }
//    //}]
//
//
//    private void sendMail(String subject, String body, String... to) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setSubject(subject);
//        message.setText(body);
//        message.setTo(to); // Accepts an array of recipients
//        mailSender.send(message);
//    }

    @RabbitListener(queues = RabbitMQConfig.USER_TO_ADMIN_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void getAllReports(ReportDTO dto) {
        try {
            reportSubject.notifyObservers(dto);
        }
        catch (Exception e) {
            System.err.println("Error notifying observers: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public List<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }

    public Admin getAdminById(String id) {
        return adminRepo.findById(id).orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    public void deleteAdminById(String id) {
        adminRepo.deleteById(id);
    }
    public Admin getAdminByUsername(String username) {
        return adminRepo.findByUsername(username);
    }

}


