package com.crepestrips.adminservice.controller;

import com.crepestrips.adminservice.model.Admin;
//import com.crepestrips.adminservice.security.JwtService;
import com.crepestrips.adminservice.service.AdminService;
import com.crepestrips.adminservice.dto.LoginRequest;
import com.crepestrips.adminservice.dto.LoginResponse;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtUtil;

    @Autowired
    public AdminController(AdminService adminService, AuthenticationManager authenticationManager) {
        this.adminService = adminService;
        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        try {
            Admin createdAdmin = adminService.createAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAdmins")
    public List<Admin> getAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/id/{id}")
    public Admin getAdminById(@PathVariable String id) {
        return adminService.getAdminById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String id) {
        try {
            adminService.deleteAdminById(id);
            return ResponseEntity.ok("Admin deleted successfully");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest authRequest) {
//        try {
//
//            // Create authentication token with provided username and password
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//
//            if (authentication.isAuthenticated()) {
//                return new LoginResponse(jwtUtil.generateToken(authRequest.getUsername()));
//            } else {
//                throw new UsernameNotFoundException("Invalid admin request!");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Invalid username or password", e);
//        }
//    }

    @PostMapping("/logout")
    public String logout(@RequestParam String username) {
        // Logout logic (e.g., invalidate session, clear context, etc.)
        SecurityContextHolder.clearContext();
        return "Admin " + username + " logged out successfully.";
    }


}