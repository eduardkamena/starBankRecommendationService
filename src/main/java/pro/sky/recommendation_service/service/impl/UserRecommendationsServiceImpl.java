package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.dto.UserRecommendationsDTO;
import pro.sky.recommendation_service.exception.UserNotFoundException;
import pro.sky.recommendation_service.repository.TransactionsRepository;
import pro.sky.recommendation_service.service.UserRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserRecommendationsServiceImpl implements UserRecommendationsService {

    private final Logger logger = LoggerFactory.getLogger(UserRecommendationsServiceImpl.class);

    private final RecommendationRuleSet[] recommendationRuleSets;
    private final TransactionsRepository transactionsRepository;

    @Autowired
    public UserRecommendationsServiceImpl(RecommendationRuleSet[] recommendationRuleSets,
                                          TransactionsRepository transactionsRepository) {
        this.recommendationRuleSets = recommendationRuleSets;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public UserRecommendationsDTO getAllRecommendations(UUID user_id) throws UserNotFoundException {

        logger.info("Starting checking user in database for user_id: {}", user_id);

        if (transactionsRepository.isUserExists(user_id)) {

            logger.info("Starting getting in List<> all recommendations for user_id: {}", user_id);
            List<Recommendations> recommendations = new ArrayList<>();

            for (RecommendationRuleSet rule : recommendationRuleSets) {
                rule.checkRecommendation(user_id)
                        .ifPresent(recommendations::add);
                logger.info("Adding result of getting recommendation to List<> for user_id: {}", user_id);
            }
            logger.info("Transferring all found recommendations from List<> to UserRecommendationsDTO for user_id: {}", user_id);
            return new UserRecommendationsDTO(user_id, recommendations);
        }

        logger.error("Error checking user in database for user_id: {}", user_id);
        throw new UserNotFoundException("User not found in database");
    }

}
