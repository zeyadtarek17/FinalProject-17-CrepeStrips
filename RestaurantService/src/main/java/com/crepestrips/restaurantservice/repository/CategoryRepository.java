package com.crepestrips.restaurantservice.repository;

import com.crepestrips.restaurantservice.model.Category;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
        Category findByName(String name);
        boolean existsByName(String name);
        void deleteByName(String name);
        Category findByIdAndName(String id, String name);
        
}
