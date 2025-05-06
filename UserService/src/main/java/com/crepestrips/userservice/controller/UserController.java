package com.crepestrips.userservice.controller;

import com.crepestrips.userservice.model.Report;
import com.crepestrips.userservice.model.User;
import com.crepestrips.userservice.service.UserService;
import com.crepestrips.userservice.singleton.UserServiceSingleton;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        UserServiceSingleton.getInstance(userService);
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String username) {
        return userService.logout(username);
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
