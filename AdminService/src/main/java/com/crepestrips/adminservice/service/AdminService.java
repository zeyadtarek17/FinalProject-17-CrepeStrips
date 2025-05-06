package com.crepestrips.adminservice.service;

import com.crepestrips.adminservice.model.Admin;
import com.crepestrips.adminservice.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;


    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;

    }


    public String login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            throw new RuntimeException("Invalid username");
        }
        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }

        return "Login successful";
    }

    public String logout(String username) {
        return "Admin " + username + " logged out successfully.";
    }




}