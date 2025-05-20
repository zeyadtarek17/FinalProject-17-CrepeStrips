package com.crepestrips.userservice.controller;

import com.crepestrips.userservice.dto.*;
import com.crepestrips.userservice.model.Cart;
import com.crepestrips.userservice.model.Report;
import com.crepestrips.userservice.model.User;
import com.crepestrips.userservice.security.JwtService;
import com.crepestrips.userservice.service.UserService;

import jakarta.validation.Valid;

import java.util.LinkedHashMap;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;
    private final UserProducer producer;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtService jwtUtil,
            UserProducer producer, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.producer = producer;
        this.userService = userService;
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<DefaultResult> checkoutAndRequestOrder(@PathVariable UUID userId) {
        try {
            // 1. Get the cart for the given userId
            Optional<Object> cartOptional = Optional.ofNullable(userService.getCartByUserId(userId).orElse(null));
            
            if (cartOptional.isEmpty()) {
                return ResponseEntity.ok(new DefaultResult("Cart not found for user ID: " + userId, true, null));
            }

            // Handle the case where it might be a LinkedHashMap
            Cart cart;
            Object cartObj = cartOptional.get();
            
            if (cartObj instanceof LinkedHashMap) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> cartMap = (LinkedHashMap<String, Object>) cartObj;
                cart = new Cart();
                
                // Extract ID - handle both String and UUID
                Object idObj = cartMap.get("id");
                if (idObj != null) {
                    cart.setId(idObj instanceof UUID ? (UUID) idObj : UUID.fromString(idObj.toString()));
                }
                
                // Extract userId - handle both String and UUID
                Object userIdObj = cartMap.get("userId");
                if (userIdObj != null) {
                    cart.setUserId(userIdObj instanceof UUID ? (UUID) userIdObj : UUID.fromString(userIdObj.toString()));
                }
                
                // Extract items list
                @SuppressWarnings("unchecked")
                List<String> items = (List<String>) cartMap.get("items");
                cart.setItems(items);
            } else {
                cart = (Cart) cartObj;
            }
            
            if (cart.getItems() == null || cart.getItems().isEmpty()) {
                return ResponseEntity.ok(new DefaultResult("Cart is empty for user ID: " + userId, true, null));
            }

            // 2. Prepare the CartDto that OrderService expects.
            CartDto cartDtoForEvent = new CartDto(
                    cart.getId(), // The cart's own ID
                    cart.getUserId(), // The user's ID (should match the path variable userId)
                    cart.getItems() // The list of food item ID strings
            );

            // Defensive check: Ensure cart's userId matches path variable userId
            if (!cart.getUserId().equals(userId)) {
                return ResponseEntity.ok(new DefaultResult("Cart does not belong to the specified user.", true, null));
            }

            // 3. Evict the cart from cache
            userService.evictCartFromCache(userId);

            // 4. Call the UserProducer to publish the event
            producer.requestOrderPlacement(cartDtoForEvent);
            return ResponseEntity.ok(new DefaultResult("Order placement request received for user " + userId + 
                    " and cart " + cart.getId() + ". It is being processed.", false, cartDtoForEvent));
                    
        } catch (Exception e) {
            e.printStackTrace(); // Add this for debugging
            return ResponseEntity.ok(new DefaultResult("Failed to initiate order placement: " + e.getMessage(), true, null));
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
    public ResponseEntity<DefaultResult> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUser user) {
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
