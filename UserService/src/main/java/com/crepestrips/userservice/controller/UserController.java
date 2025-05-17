package com.crepestrips.userservice.controller;

import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.model.Report;
import com.crepestrips.userservice.model.User;
import com.crepestrips.userservice.security.JwtService;
import com.crepestrips.userservice.service.UserService;
import com.crepestrips.userservice.UserServiceSingleton;
import com.crepestrips.userservice.client.FoodItemClient;
import com.crepestrips.userservice.dto.AuthRequest;
import com.crepestrips.userservice.dto.AuthResponse;
import com.crepestrips.userservice.dto.ChangePasswordRequest;
import com.crepestrips.userservice.dto.FoodItemResponse;
import com.crepestrips.userservice.dto.UserMessage;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.crepestrips.userservice.service.UserProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;
    private final UserProducer producer;
    private final FoodItemClient foodItemClient;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtUtil,
            UserProducer producer, FoodItemClient foodItemClient) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.producer = producer;
        this.foodItemClient = foodItemClient;
    }

    @PostMapping("/order/add")
    public ResponseEntity<String> createOrder(@RequestBody UUID userId) {
        // get the cart by userId
        Optional<Cart> cart = userService.getCartByUserId(userId);
        if (cart.isEmpty()) {
            return ResponseEntity.badRequest().body("Cart not found for user ID: " + userId);
        }
        // get the list of item ids from the cart
        List<String> itemIds = cart.get().getItems();
        // call the endpoint that retrieves list of fooditems (sync) api
        List<FoodItemResponse> items = null;
        try {
            items = foodItemClient.getItemsById(itemIds).getBody();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving food items: " + e.getMessage());
        }
        // extract the restaurant id from the first item
        if (itemIds.isEmpty()) {
            return ResponseEntity.badRequest().body("No items found in the cart for user ID: " + userId);
        }
        String restaurantId = items.get(0).getRestaurantId();
        // generate orderid to be tracked
        UUID orderId = UUID.randomUUID();
        // send the message to the order service
        producer.createOrder(userId, restaurantId, items, orderId);
        // now these data to order service(async using rabbitmq)
        return ResponseEntity.ok("Order being created with ID: " + orderId);
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        try {

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
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request.getUserName(), request.getOldPassword(), request.getNewPassword());
    }

    @PostMapping("/report")
    public ResponseEntity<Report> reportIssue(@RequestParam UUID userId,
                                              @RequestParam String type,
                                              @RequestParam String content,
                                              @RequestParam String targetId) {
        Report savedReport = userService.reportIssue(userId, type, content, targetId);
        return ResponseEntity.ok(savedReport);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

    @GetMapping("cart/{userId}")
    public Optional<Cart> getCartByUserId(@PathVariable UUID userId) {
        return userService.getCartByUserId(userId);
    }

    @PostMapping("/cart")
    public Cart saveCart(@Valid @RequestBody Cart cart) {
        return userService.saveCart(cart);
    }
}
