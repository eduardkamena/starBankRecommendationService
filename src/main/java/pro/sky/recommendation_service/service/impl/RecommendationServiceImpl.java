package pro.sky.recommendation_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.repository.RecommendationsRepository;
import pro.sky.recommendation_service.service.RecommendationService;

import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationsRepository recommendationsRepository;

    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public int getRandomTransactionAmount(UUID user) {
        logger.info("Random transaction amount fot user{} has been successfully get", user);
        return recommendationsRepository.getRandomTransactionAmount(user);
    }

}
