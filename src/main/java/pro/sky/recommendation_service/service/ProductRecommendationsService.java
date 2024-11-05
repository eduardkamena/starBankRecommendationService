package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;

import java.util.List;
import java.util.UUID;

public interface ProductRecommendationsService {

    List<ProductRecommendationsDTO> getRecommendationProduct(UUID product_id);

}
