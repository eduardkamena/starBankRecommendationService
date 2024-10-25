package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.entity.Product;
import pro.sky.recommendation_service.repository.ProductsRepository;
import pro.sky.recommendation_service.service.ProductsService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;


    @Override
    public Product createProduct(Product product) {
        productsRepository.addProduct(product);
        return product;
    }

    @Override
    public List<Product> getAllProductsForRecommendation() {
        return List.of();
    }

    @Override
    public void deleteProduct(UUID productId) {
        productsRepository.deleteProductById(productId);
    }
}
