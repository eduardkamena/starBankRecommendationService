package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для работы с продуктами рекомендаций.
 * <p>
 * Определяет метод для получения продукта рекомендации по его ID.
 */
public interface ProductRecommendationsService {

    /**
     * Получение продукта рекомендации по ID.
     *
     * @param productId идентификатор продукта
     * @return список продуктов рекомендации
     */
    List<ProductRecommendationsDTO> getRecommendationProduct(UUID productId);

}
