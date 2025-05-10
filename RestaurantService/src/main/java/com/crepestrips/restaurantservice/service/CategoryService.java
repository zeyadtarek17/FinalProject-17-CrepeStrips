package com.crepestrips.restaurantservice.service;


import com.crepestrips.restaurantservice.model.Category;
import com.crepestrips.restaurantservice.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(String id) {
        return categoryRepository.findById(id);
    }

    public Category create(Category category) {
        if (category.getId() == null) {
            return categoryRepository.save(category);  // Save, MongoDB will generate an ID.
        }
        return categoryRepository.save(category);
    }

    public Optional<Category> update(String id, Category updatedCategory) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setName(updatedCategory.getName());
            return categoryRepository.save(existing);
        });
    }

    public boolean delete(String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
