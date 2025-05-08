package com.crepestrips.adminservice.repository;

import com.crepestrips.adminservice.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {

    Optional<Admin> findByUsername(String username);

    boolean existsByUsername(String username);
}