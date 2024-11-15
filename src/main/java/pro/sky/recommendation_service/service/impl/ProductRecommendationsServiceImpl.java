package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.exception.ProductNotFoundException;
import pro.sky.recommendation_service.repository.ProductRecommendationsRepository;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <h3>Класс для работы с продуктами рекомендаций.
 *
 * <p>В данной реализации позволяет получить сам продукт из рекомендации.
 */
@Service
public class ProductRecommendationsServiceImpl implements ProductRecommendationsService {

    private final Logger logger = LoggerFactory.getLogger(ProductRecommendationsServiceImpl.class);

    private final ProductRecommendationsRepository productRecommendationsRepository;

    @Autowired
    public ProductRecommendationsServiceImpl(ProductRecommendationsRepository productRecommendationsRepository) {
        this.productRecommendationsRepository = productRecommendationsRepository;
    }

    /**
     * Метод получения продукта рекомендации
     * <p>
     *     Метод нахождения и получения продукта рекомендации
     *     и приведении его к виду DTO (name, productId, text)
     * </p>
     * @param productId ID продукта по БД {@link Recommendations Recommendations}
     * @return продукт со списком рекомендаций, приведенный к виду ({@link RecommendationsDTO})
     * @throws ProductNotFoundException если продукт не найден в БД
     */
    @Override
    @Caching( cacheable = {
            @Cacheable(cacheNames = "productRecommendations", key = "#root.methodName + #productId.toString()"),
            @Cacheable(cacheNames = "fixedRecommendations", key = "#root.methodName + #productId.toString()")
    })
    public List<ProductRecommendationsDTO> getRecommendationProduct(UUID productId) throws ProductNotFoundException {

        logger.info("Starting checking product in database for productId: {}", productId);
        if (productRecommendationsRepository.existsByProductId(productId)) {

            logger.info("Product {} successfully found in database and transferred to List<>", productId);
            return productRecommendationsRepository.findByProductId(productId)
                    .stream()
                    .map(recommendation -> {
                        ProductRecommendationsDTO dto = new ProductRecommendationsDTO();
                        dto.setProductName((String) recommendation[0]);
                        dto.setProductId((UUID) recommendation[1]);
                        dto.setProductText((String) recommendation[2]);
                        return dto;
                    }).collect(Collectors.toList());
        }
        logger.error("Error checking product in database for productId: {}", productId);
        throw new ProductNotFoundException("Product not found in database");
    }

}
