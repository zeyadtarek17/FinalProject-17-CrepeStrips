package com.crepestrips.userservice.controller;

import com.crepestrips.userservice.model.Report;
import com.crepestrips.userservice.model.User;
import com.crepestrips.userservice.security.JwtService;
import com.crepestrips.userservice.service.UserService;
import com.crepestrips.userservice.UserServiceSingleton;
import com.crepestrips.userservice.dto.AuthRequest;
import com.crepestrips.userservice.dto.AuthResponse;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        UserServiceSingleton.getInstance(userService);
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        try {

            // check if user exists
            // String user = userService.login(authRequest.getUsername(), authRequest.getPassword());
            // Create authentication token with provided username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                return new AuthResponse(jwtUtil.generateToken(authRequest.getUsername()));
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password", e);
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String username) {
        // Logout logic (e.g., invalidate session, clear context, etc.)
        SecurityContextHolder.clearContext();
        return "User " + username + " logged out successfully.";
    }

    @PutMapping("/password")
    public String changePassword(@RequestParam Long userId, @RequestParam String newPassword) {
        return userService.changePassword(userId, newPassword);
    }

    @PostMapping("/{userId}/report")
    public Report reportIssue(@PathVariable Long userId, @RequestBody Report report) {
        return userService.reportIssue(userId, report);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
