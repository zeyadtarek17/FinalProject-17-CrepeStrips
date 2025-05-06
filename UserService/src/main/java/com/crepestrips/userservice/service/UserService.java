package com.crepestrips.userservice.service;

import com.crepestrips.userservice.model.User;
import com.crepestrips.userservice.model.Report;
import com.crepestrips.userservice.repository.UserRepository;
import com.crepestrips.userservice.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    @Autowired
    public UserService(UserRepository userRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        // Optional: rebuild via Builder to enforce pattern
        User builtUser = new User.Builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        return userRepository.save(builtUser);
    }



    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }

        return "Login successful";
    }




    public String logout(String username) {
        return "User " + username + " logged out successfully.";
    }

    public String changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);
        userRepository.save(user);
        return "Password updated.";
    }


    public Report reportIssue(Long userId, Report report) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        report.setUser(user);
        return reportRepository.save(report);
    }


    @Cacheable(value = "Users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @CachePut(value = "Users", key = "#result.id")
    public User updateUser(Long id, User newData) {
        newData.setId(id);
        return userRepository.save(newData);
    }
    @CacheEvict(value = "Users", key = "#id")
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted.";
    }
}
