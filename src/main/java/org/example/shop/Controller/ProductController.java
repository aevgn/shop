package org.example.shop.Controller;

import org.example.shop.DTO.product.CreateProductRequest;
import org.example.shop.DTO.product.ProductResponse;
import org.example.shop.DTO.product.UpdateProductRequest;
import org.example.shop.Service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse addProduct(@RequestBody CreateProductRequest request){
        return productService.addProduct(request);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request){
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id){
        return productService.getProduct(id);
    }

}
