package com.crepestrips.adminservice.service;

import com.crepestrips.adminservice.model.Admin;
import com.crepestrips.adminservice.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepo,PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {
        Admin admin = adminRepo.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return "Login successful";
    }

    public String logout(String username) {
        return "Admin " + username + " logged out successfully.";
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepo.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(
                admin.getUsername(),
                admin.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) // Or map user roles/authorities here
        );
    }



}


