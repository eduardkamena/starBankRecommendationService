package pro.sky.recommendation_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.repository.ProductRecommendationsRepository;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductRecommendationsServiceImpl implements ProductRecommendationsService {

    private final ProductRecommendationsRepository productRecommendationsRepository;

    @Autowired
    public ProductRecommendationsServiceImpl(ProductRecommendationsRepository productRecommendationsRepository) {
        this.productRecommendationsRepository = productRecommendationsRepository;
    }

    @Override
    public List<ProductRecommendationsDTO> getByProductId(UUID product_id) {
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

}
