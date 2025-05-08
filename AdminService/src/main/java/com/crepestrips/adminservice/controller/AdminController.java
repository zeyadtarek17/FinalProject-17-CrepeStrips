package com.crepestrips.adminservice.controller;

import com.crepestrips.adminservice.model.Admin;
import com.crepestrips.adminservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody String username, @RequestBody String password) {
        boolean isAuthenticated = adminService.authenticateAdmin(username, password);
        if (isAuthenticated) {
            return ResponseEntity.ok().body("Login successful");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    @GetMapping("/getAdmins")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

}