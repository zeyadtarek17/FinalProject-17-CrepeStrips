package com.crepestrips.adminservice.controller;

import com.crepestrips.adminservice.client.FoodItemServiceClient;
import com.crepestrips.adminservice.client.RestaurantServiceClient;
import com.crepestrips.adminservice.command.*;
import com.crepestrips.adminservice.dto.*;
import com.crepestrips.adminservice.model.Admin;
import com.crepestrips.adminservice.security.JwtService;
import com.crepestrips.adminservice.service.AdminService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;
    private final AdminInvoker invoker;
    private final FoodItemServiceClient foodItemServiceClient;
    private final RestaurantServiceClient restaurantServiceClient;


    @Autowired
    public AdminController(AdminService adminService, AuthenticationManager authenticationManager, JwtService jwtUtil, AdminInvoker invoker, FoodItemServiceClient foodItemServiceClient, RestaurantServiceClient restaurantServiceClient) {
        this.adminService = adminService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.invoker = invoker;
        this.foodItemServiceClient = foodItemServiceClient;
        this.restaurantServiceClient = restaurantServiceClient;
    }
    @PostMapping("/create")
    public ResponseEntity<DefaultResult> createAdmin(@RequestBody Admin admin) {
        try {
            Admin createdAdmin = adminService.createAdmin(admin);
            return ResponseEntity.ok(new DefaultResult("Admin registered successfully", false, createdAdmin));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @GetMapping("/getAdmins")
    public ResponseEntity<DefaultResult> getAdmins() {
        try {
            List<Admin> admins = adminService.getAllAdmins();
            return ResponseEntity.ok(new DefaultResult("Admins found", false, admins));

        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DefaultResult> getAdminById(@PathVariable String id) {
        try {
            Admin admin = adminService.getAdminById(id);
            return ResponseEntity.ok(new DefaultResult("Admin found", false, admin));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DefaultResult> deleteAdmin(@PathVariable String id) {
        try {
           adminService.deleteAdminById(id);
            return ResponseEntity.ok(new DefaultResult("Admin deleted successfully", false, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<DefaultResult> login(@RequestBody LoginRequest authRequest) {
        try {

            // Create authentication token with provided username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                adminService.login(authRequest.getUsername());
                LoginResponse response = new LoginResponse(jwtUtil.generateToken(authRequest.getUsername()));
                return ResponseEntity.ok(new DefaultResult("Admin logged in successfully", false, response));
            } else {
                return ResponseEntity.ok(new DefaultResult("Invalid username or password", true, null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/logout/{userId}")
    public ResponseEntity<DefaultResult> logout(@PathVariable String userId) {
        // Logout logic (e.g., invalidate session, clear context, etc.)
        // SecurityContextHolder.clearContext();
        // return "User " + username + " logged out successfully.";
        try {
            adminService.logout(userId);
            return ResponseEntity.ok(new DefaultResult("User logged out successfully", false, null));

        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }
    @PostMapping("/fooditems/{id}/suspend")
    public ResponseEntity<DefaultResult> suspendFoodItem(@PathVariable String id) {

        try {
            AdminCommand command = new SuspendFoodItemCommand(foodItemServiceClient, id);
            invoker.setCommand(command);
            invoker.executeCommand();
            return ResponseEntity.ok(new DefaultResult("Fooditem suspended successfully", false, null));


        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @PostMapping("/restaurants/{id}/ban")
    public ResponseEntity<DefaultResult> banRestaurant(@PathVariable String id) {
        try {
            AdminCommand command = new BanRestaurantCommand(restaurantServiceClient, id);
            invoker.setCommand(command);
            invoker.executeCommand();
            return ResponseEntity.ok(new DefaultResult("Restaurant banned successfully", false, null));


        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }

    }
    @PostMapping("/fooditems/{id}/unsuspend")
    public ResponseEntity<DefaultResult> unsuspendFoodItem(@PathVariable String id) {

        try {
            AdminCommand command = new UnsuspendFoodItemCommand(foodItemServiceClient, id);
            invoker.setCommand(command);
            invoker.executeCommand();
            return ResponseEntity.ok(new DefaultResult("fooditem Unsuspended logged in successfully", false, null));


        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @PostMapping("/restaurants/{id}/unban")
    public ResponseEntity<DefaultResult> unbanRestaurant(@PathVariable String id) {
        try {
            AdminCommand command = new UnbanRestaurantCommand(restaurantServiceClient, id);
            invoker.setCommand(command);
            invoker.executeCommand();
            return ResponseEntity.ok(new DefaultResult("restaurant unbanned", false, null));


        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }

    }



}