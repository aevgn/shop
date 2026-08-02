package org.example.shop.Controller;

import org.example.shop.DTO.category.CategoryResponse;
import org.example.shop.DTO.category.CreateCategoryRequest;
import org.example.shop.DTO.category.UpdateCategoryRequest;
import org.example.shop.Service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @PostMapping
    public CategoryResponse addCategory(@RequestBody CreateCategoryRequest request){
        return categoryService.addCategory(request);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable Long id){
        return categoryService.getCategory(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryRequest request){
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }
}
