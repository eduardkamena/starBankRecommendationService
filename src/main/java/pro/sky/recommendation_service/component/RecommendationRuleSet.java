package pro.sky.recommendation_service.component;

import pro.sky.recommendation_service.entity.Recommendations;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<Recommendations> checkRecommendation(UUID user_id);

}
