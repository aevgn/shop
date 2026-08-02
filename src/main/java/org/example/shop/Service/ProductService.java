package org.example.shop.Service;

import org.example.shop.DTO.product.CreateProductRequest;
import org.example.shop.DTO.product.ProductResponse;
import org.example.shop.DTO.product.UpdateProductRequest;
import org.example.shop.Entity.Category;
import org.example.shop.Entity.Product;
import org.example.shop.Repository.CategoryRepository;
import org.example.shop.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public  ProductService(ProductRepository productRepository, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse addProduct(CreateProductRequest request){
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow();

        Product savedProduct = new Product();

        savedProduct.setName(request.getName());
        savedProduct.setPrice(request.getPrice());
        savedProduct.setQuantity(request.getQuantity());
        savedProduct.setDescription(request.getDescription());
        savedProduct.setCategory(category);

        savedProduct = productRepository.save(savedProduct);

        return toResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return toResponse(updatedProduct);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.deleteById(id);
    }

    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    private ProductResponse toResponse(Product product){
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        return response;
    }

    public ProductResponse getProduct(Long id){
        return toResponse(productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found")));
    }

}
