package com.crepestrips.userservice.service;

import com.crepestrips.userservice.model.User;
import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.model.Report;
import com.crepestrips.userservice.repository.UserRepository;
import com.crepestrips.userservice.repository.CartRepository;
import com.crepestrips.userservice.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.crepestrips.userservice.dto.ReportDTO;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    @Autowired
    private ReportProducer reportProducer;

    @Autowired
    public UserService(UserRepository userRepository, ReportRepository reportRepository,
                       PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    public Report reportIssue(UUID userId, String type, String content, String targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = new Report();
        report.setUser(user);
        report.setType(type);
        report.setContent(content);
        report.setTargetId(targetId);
        report.setCreatedAt(new Date());

        Report saved = reportRepository.save(report);

        ReportDTO dto = new ReportDTO();
        dto.setId(saved.getId());
        dto.setUserId(user.getId());
        dto.setType(saved.getType());
        dto.setContent(saved.getContent());
        dto.setTargetId(saved.getTargetId());
        dto.setCreatedAt(saved.getCreatedAt());

        reportProducer.sendReport(dto);

        return saved;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User builtUser = new User.Builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        return userRepository.save(builtUser);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return "Login successful";
    }

    public String logout(String username) {
        return "User " + username + " logged out successfully.";
    }

    public String changePassword(String userName, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password updated.";
    }

    /*public Report reportIssue(UUID userId, Report report) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        report.setUser(user);
        return reportRepository.save(report);
    }*/

    @Cacheable(value = "Users", key = "#id")
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @CachePut(value = "Users", key = "#result.id")
    public User updateUser(UUID id, User newData) {
        newData.setId(id);
        return userRepository.save(newData);
    }

    @CacheEvict(value = "Users", key = "#id")
    public String deleteUser(UUID id) {
        userRepository.deleteById(id);
        return "User deleted.";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username) // adjust method
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")) // Or map user roles/authorities here
        );
    }

    //cart

    @Cacheable(value = "Carts", key = "#userId")
    public Optional<Cart> getCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    @CachePut(value = "Carts", key = "#result.userId")
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @CacheEvict(value = "Carts", key = "#userId")
    public void evictCartFromCache(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null.");
        }
        // Additional logic can be added here if needed, such as logging
        System.out.println("Cart evicted from cache for userId: " + userId);
    }
}