package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductsService {

    Product createProduct(Product product);
    List<Product> getAllProductsForRecommendation();
    void deleteProduct(UUID productId);

}
