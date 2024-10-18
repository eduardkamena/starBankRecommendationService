package pro.sky.recommendation_service.component;

import pro.sky.recommendation_service.dto.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<RecommendationDTO> getRecommendation(UUID user_id);

}
