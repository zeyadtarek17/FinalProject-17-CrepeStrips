package com.crepestrips.userservice.controller;

import com.crepestrips.userservice.dto.*;
import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.model.Report;
import com.crepestrips.userservice.model.User;
import com.crepestrips.userservice.security.JwtService;
import com.crepestrips.userservice.service.UserService;
import com.crepestrips.userservice.client.FoodItemClient;

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
    public ResponseEntity<DefaultResult> register(@Valid @RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(new DefaultResult("User registered successfully", false, savedUser));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<DefaultResult> login(@RequestBody AuthRequest authRequest) {
        try {

            // Create authentication token with provided username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                userService.login(authRequest.getUsername());
                AuthResponse response = new AuthResponse(jwtUtil.generateToken(authRequest.getUsername()));
                return ResponseEntity.ok(new DefaultResult("User logged in successfully", false, response));
            } else {
                return ResponseEntity.ok(new DefaultResult("Invalid username or password", true, null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/logout/{userId}")
    public ResponseEntity<DefaultResult> logout(@PathVariable UUID userId) {
        // Logout logic (e.g., invalidate session, clear context, etc.)
        // SecurityContextHolder.clearContext();
        // return "User " + username + " logged out successfully.";
        try {
            userService.logout(userId);
            return ResponseEntity.ok(new DefaultResult("User logged out successfully", false, null));

        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<DefaultResult> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            String res = userService.changePassword(request.getUserName(), request.getOldPassword(),
                    request.getNewPassword());
            return ResponseEntity.ok(new DefaultResult(res, false, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }

    }

    @PostMapping("/report")
    public ResponseEntity<DefaultResult> reportIssue(@RequestBody ReportDTO reportDTO) {
        try {
            Report savedReport = userService.reportIssue(
                    reportDTO.getUserId(), reportDTO.getType(), reportDTO.getContent(), reportDTO.getTargetId());

            ReportDTO response = new ReportDTO(
                    savedReport.getId(),
                    savedReport.getUser().getId(),
                    savedReport.getContent(),
                    savedReport.getType(),
                    savedReport.getTargetId(),
                    savedReport.getCreatedAt());

            return ResponseEntity.ok(new DefaultResult("Report created successfully", false, response));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultResult> getUserById(@PathVariable UUID id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new DefaultResult("User found", false, user));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("/all")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DefaultResult> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(new DefaultResult("Users found", false, users));

        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DefaultResult> updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        try {
            User res = userService.updateUser(id, user);
            return ResponseEntity.ok(new DefaultResult("User updated successfully", false, res));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<DefaultResult> deleteUser(@PathVariable UUID id) {
        try {
            String res = userService.deleteUser(id);
            return ResponseEntity.ok(new DefaultResult("User deleted successfully", false, res));
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }
    }

    @GetMapping("cart/{userId}")
    public ResponseEntity<DefaultResult> getCartByUserId(@PathVariable UUID userId) {
        try {
            Optional<Cart> cart = userService.getCartByUserId(userId);
            if (cart.isPresent()) {
                return ResponseEntity.ok(new DefaultResult("Cart found", false, cart.get()));
            } else {
                return ResponseEntity.ok(new DefaultResult("Cart not found", true, null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));
        }

    }

    @PostMapping("/cart")
    public ResponseEntity<DefaultResult> saveCart(@Valid @RequestBody Cart cart) {
        try {
            Cart res = userService.saveCart(cart);
            return ResponseEntity.ok(new DefaultResult("Cart updated successfully", false, res));

        } catch (Exception e) {
            return ResponseEntity.ok(new DefaultResult(e.getMessage(), true, null));

        }
    }
}
