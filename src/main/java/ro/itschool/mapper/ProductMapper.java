package ro.itschool.mapper;

import org.springframework.stereotype.Component;
import ro.itschool.controller.model.ProductDTO;
import ro.itschool.entity.Product;

@Component
public class ProductMapper {

    public ProductDTO fromEntity(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    public Product toEntity(ProductDTO productDTO) {
        return new Product(productDTO.getName(), productDTO.getPrice(), productDTO.getQuantity());
    }

}
