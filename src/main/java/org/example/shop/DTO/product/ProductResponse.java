package org.example.shop.DTO.product;
import lombok.Getter;
import lombok.Setter;
import org.example.shop.Entity.Category;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Long categoryId;
    private String categoryName;
}
