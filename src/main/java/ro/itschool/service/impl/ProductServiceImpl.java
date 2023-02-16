package ro.itschool.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.itschool.controller.model.ProductDTO;
import ro.itschool.entity.Product;
import ro.itschool.mapper.ProductMapper;
import ro.itschool.repository.ProductRepository;
import ro.itschool.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public void save(ProductDTO productDTO) {
        productRepository.save(productMapper.toEntity(productDTO));
    }

    @Override
    public Optional<ProductDTO> findById(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(productMapper::fromEntity);
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> findByQuantityGreaterThan(Long quantity) {
        return productRepository.findByQuantityGreaterThan(quantity)
                .stream()
                .map(productMapper::fromEntity)
                .toList();
    }
}
