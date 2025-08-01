package com.crepestrips.userservice.service;

import com.crepestrips.userservice.model.User;
import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.model.Report;
import com.crepestrips.userservice.repository.UserRepository;
import com.crepestrips.userservice.repository.CartRepository;
import com.crepestrips.userservice.repository.ReportRepository;
import com.crepestrips.userservice.session.LoginSessionManager;
import com.crepestrips.userservice.util.BeanMapperUtils;
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

import com.crepestrips.userservice.client.FoodItemServiceClient;
import com.crepestrips.userservice.dto.DefaultResult;
import com.crepestrips.userservice.dto.ReportDTO;
import com.crepestrips.userservice.dto.UpdateUser;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final FoodItemServiceClient foodItemServiceClient;

    @Autowired
    private ReportProducer reportProducer;

    @Autowired
    public UserService(UserRepository userRepository, ReportRepository reportRepository,
            PasswordEncoder passwordEncoder, CartRepository cartRepository,
            FoodItemServiceClient foodItemServiceClient) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.foodItemServiceClient = foodItemServiceClient;
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

        user.getReports().add(saved);
        userRepository.save(user);

        ReportDTO dto = new ReportDTO();
        BeanMapperUtils.copyFields(saved, dto);
        dto.setUserId(user.getId()); // manually set userId since it's nested in Report
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

    public void login(String username) {
        if (LoginSessionManager.getInstance().isLoggedIn(username)) {
            throw new RuntimeException("User already logged in");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        user.setLoggedIn(true);
        userRepository.save(user);
        System.out.println("[LoginManager] User '" + username + "' logged in.");
        LoginSessionManager.getInstance().login(username); // register login
    }

    public void logout(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLoggedIn(false);
        userRepository.save(user);

        LoginSessionManager.getInstance().logout(user.getUsername()); // unregister login
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

    /*
     * public Report reportIssue(UUID userId, Report report) {
     * User user = userRepository.findById(userId)
     * .orElseThrow(() -> new RuntimeException("User not found"));
     *
     * report.setUser(user);
     * return reportRepository.save(report);
     * }
     */

    @Cacheable(value = "Users", key = "#id", unless = "#result == null || !#result.isPresent()")
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(UUID id, UpdateUser newData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newData.getEmail() != null) {
            user.setEmail(newData.getEmail());
        }
        if (newData.getFirstName() != null) {
            user.setFirstName(newData.getFirstName());
        }
        if (newData.getLastName() != null) {
            user.setLastName(newData.getLastName());
        }

        return userRepository.save(user);
    }

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

    // cart

    @Cacheable(value = "Carts", key = "#userId")
    public Optional<Cart> getCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    @CachePut(value = "Carts", key = "#result.userId")
    public Cart saveCart(Cart cart) {
        // check if user has a cart first, if he has saving the cart
        // if he doesn't have a cart, create a new one
        // if he has a cart, update the cart
        // if he has a cart, add the item to the cart

        Optional<Cart> existingCart = cartRepository.findByUserId(cart.getUserId());
        if (existingCart.isPresent()) {
            Cart cartToUpdate = existingCart.get();
            cartToUpdate.setItems(cart.getItems());
            // clean cart
            List<String> ids = cartToUpdate.getItems();
            DefaultResult res = foodItemServiceClient.cleanCart(ids);
            if (res.isError()) {
                throw new RuntimeException(res.getMessage());
            }
            List<String> cleanedIds = (List<String>) res.getResult();
            cartToUpdate.setItems(cleanedIds);

            return cartRepository.save(cartToUpdate);
        } else {
            DefaultResult res = foodItemServiceClient.cleanCart(cart.getItems());
            if (res.isError()) {
                throw new RuntimeException(res.getMessage());
            }
            List<String> cleanedIds = (List<String>) res.getResult();
            cart.setItems(cleanedIds);
            return cartRepository.save(cart);
        }
    }

    @CacheEvict(value = "Carts", key = "#userId")
    public void evictCartFromCache(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null.");
        }
        // Additional logic can be added here if needed, such as logging
        System.out.println("Cart evicted from cache for userId: " + userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username) // adjust method
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}