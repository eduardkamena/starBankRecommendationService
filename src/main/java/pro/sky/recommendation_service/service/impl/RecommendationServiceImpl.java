package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.service.RecommendationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationRuleSet[] recommendationRuleSets;

    @Autowired
    public RecommendationServiceImpl(RecommendationRuleSet[] recommendationRuleSets) {
        this.recommendationRuleSets = recommendationRuleSets;
    }

    @Override
    public UserRecommendationsDTO getAllRecommendations(UUID user_id) {

        logger.info("Start getting in List<> all recommendations for user_id: {}", user_id);
        List<RecommendationDTO> recommendations = new ArrayList<>();

        for (RecommendationRuleSet rule : recommendationRuleSets) {
            rule.checkRecommendation(user_id)
                    .ifPresent(recommendations::add);
            logger.info("Adding result of getting recommendation to List<> for user_id: {}", user_id);
        }
        logger.info("Transferring all found recommendations from List<> to UserRecommendationsDTO for user_id: {}", user_id);
        return new UserRecommendationsDTO(user_id, recommendations);
    }

}
