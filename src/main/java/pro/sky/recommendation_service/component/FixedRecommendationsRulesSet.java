package pro.sky.recommendation_service.component;

import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FixedRecommendationsRulesSet {

    Optional<List<ProductRecommendationsDTO>> checkRecommendation(UUID user_id);

}
