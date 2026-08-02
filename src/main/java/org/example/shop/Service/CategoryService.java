package org.example.shop.Service;

import org.example.shop.DTO.category.CategoryResponse;
import org.example.shop.DTO.category.CreateCategoryRequest;
import org.example.shop.DTO.category.UpdateCategoryRequest;
import org.example.shop.Entity.Category;
import org.example.shop.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    public CategoryResponse addCategory(CreateCategoryRequest request){
        Category category = new Category();
        category.setName(request.getName());
        category = categoryRepository.save(category);
        return toResponse(category);
    }

    private CategoryResponse toResponse(Category category){
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CategoryResponse getCategory(Long id) {
        return toResponse(categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found")));
    }

    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(request.getName());
        Category updatedCategory = categoryRepository.save(category);
        return toResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}
