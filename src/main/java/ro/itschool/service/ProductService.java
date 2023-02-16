package ro.itschool.service;

import ro.itschool.controller.model.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    void save(ProductDTO productDTO);

    Optional<ProductDTO> findById(Integer id);

    void deleteById(Integer id);

    List<ProductDTO> findByQuantityGreaterThan(Long quantity);
}
