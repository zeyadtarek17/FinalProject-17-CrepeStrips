package com.crepestrips.adminservice.repository;
import com.crepestrips.adminservice.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findByUsername(String username);

    Admin existsByUsername(String username);

}
