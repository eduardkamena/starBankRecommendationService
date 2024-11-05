package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.exception.ProductNotFoundException;
import pro.sky.recommendation_service.repository.ProductRecommendationsRepository;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductRecommendationsServiceImpl implements ProductRecommendationsService {

    private final Logger logger = LoggerFactory.getLogger(ProductRecommendationsServiceImpl.class);

    private final ProductRecommendationsRepository productRecommendationsRepository;

    @Autowired
    public ProductRecommendationsServiceImpl(ProductRecommendationsRepository productRecommendationsRepository) {
        this.productRecommendationsRepository = productRecommendationsRepository;
    }

    @Override
    public List<ProductRecommendationsDTO> getRecommendationProduct(UUID product_id) throws ProductNotFoundException {

        logger.info("Starting checking product in database for product_id: {}", product_id);
        if (productRecommendationsRepository.existsByProductId(product_id)) {

            logger.info("Product {} successfully found in database and transferred to List<>", product_id);
            return productRecommendationsRepository.findByProductId(product_id)
                    .stream()
                    .map(recommendation -> {
                        ProductRecommendationsDTO dto = new ProductRecommendationsDTO();
                        dto.setProduct_name((String) recommendation[0]);
                        dto.setProduct_id((UUID) recommendation[1]);
                        dto.setProduct_text((String) recommendation[2]);
                        return dto;
                    }).collect(Collectors.toList());
        }
        logger.error("Error checking product in database for product_id: {}", product_id);
        throw new ProductNotFoundException("Product not found in database");
    }

}
