package com.crepestrips.adminservice.controller;

import com.crepestrips.adminservice.client.FoodItemServiceClient;
import com.crepestrips.adminservice.client.RestaurantServiceClient;
import com.crepestrips.adminservice.command.AdminCommand;
import com.crepestrips.adminservice.command.AdminInvoker;
import com.crepestrips.adminservice.command.BanRestaurantCommand;
import com.crepestrips.adminservice.command.SuspendFoodItemCommand;
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

@RestController
@RequestMapping("/api/admin")
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

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest authRequest) {
        try {

            // Create authentication token with provided username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                return new LoginResponse(jwtUtil.generateToken(authRequest.getUsername()));
            } else {
                throw new UsernameNotFoundException("Invalid admin request!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password", e);
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String username) {
        // Logout logic (e.g., invalidate session, clear context, etc.)
        SecurityContextHolder.clearContext();
        return "Admin " + username + " logged out successfully.";
    }
    @PostMapping("/suspend-food")
    public ResponseEntity<CommandResponse> suspendFoodItem(@RequestBody SuspendFoodRequest request) {

        AdminCommand command = new SuspendFoodItemCommand(foodItemServiceClient, request.getFoodItemId());
        invoker.executeCommand(command);
        return ResponseEntity.ok(new CommandResponse("SUCCESS", request.getFoodItemId(), "SUSPEND_FOOD"));

    }

    @PostMapping("/ban-restaurant")
    public ResponseEntity<CommandResponse> banRestaurant(@RequestBody BanRestaurantRequest request) {

        AdminCommand command = new BanRestaurantCommand(restaurantServiceClient, request.getRestaurantId());
        invoker.executeCommand(command);
        return ResponseEntity.ok(new CommandResponse("SUCCESS", "Restaurant banned", "BAN_RESTAURANT"));

    }



}