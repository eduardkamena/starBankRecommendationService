package pro.sky.recommendation_service.component.recommendationRule;

import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.dto.RecommendationDTO;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRule implements RecommendationRuleSet {

    @Override
    public Optional<RecommendationDTO> getRecommendation(UUID user_id) {
        return Optional.empty();
    }

}
