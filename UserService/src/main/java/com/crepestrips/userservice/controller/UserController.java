package com.crepestrips.userservice.controller;

import com.crepestrips.userservice.dto.*;
import com.crepestrips.userservice.dto.CartDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.crepestrips.userservice.service.UserProducer;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;
    private final UserProducer producer;
    private final FoodItemClient foodItemClient;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtService jwtUtil,
            UserProducer producer, FoodItemClient foodItemClient, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.producer = producer;
        this.foodItemClient = foodItemClient;
        this.userService = userService;
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<String> checkoutAndRequestOrder(@PathVariable UUID userId) {
        // 1. Get the cart for the given userId
        Optional<Cart> cartOptional = userService.getCartByUserId(userId);

        if (cartOptional.isEmpty()) {
            return ResponseEntity.ok().body("Cart not found for user ID: " + userId);
        }

        Cart cart = cartOptional.get();
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return ResponseEntity.ok().body("Cart is empty for user ID: " + userId);
        }

        // 2. Prepare the CartDto that OrderService expects.
        CartDto cartDtoForEvent = new CartDto(
                cart.getId(), // The cart's own ID
                cart.getUserId(), // The user's ID (should match the path variable userId)
                cart.getItems() // The list of food item ID strings
        );

        // Defensive check: Ensure cart's userId matches path variable userId
        if (!cart.getUserId().equals(userId)) {
            // This case should ideally not happen if getCartByUserId is correct,
            return ResponseEntity.status(403).body("Cart does not belong to the specified user.");
        }

        // 3. Call the UserProducer to publish the event
        try {
            producer.requestOrderPlacement(cartDtoForEvent);
            return ResponseEntity.accepted().body("Order placement request received for user " + userId + " and cart "
                    + cart.getId() + ". It is being processed.");
        } catch (Exception e) {
            return ResponseEntity.ok().body("Failed to initiate order placement: " + e.getMessage());
        }
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
