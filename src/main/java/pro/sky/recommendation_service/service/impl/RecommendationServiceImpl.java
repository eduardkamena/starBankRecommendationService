package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.repository.RecommendationsRepository;
import pro.sky.recommendation_service.service.RecommendationService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final Logger logger = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationsRepository recommendationsRepository;

    @Override
    public int getRandomTransactionAmount(UUID user_id, String transactionType, String productsType) {
        logger.info("Random transaction amount fot user{} has been successfully get", user_id);
        return recommendationsRepository.getRandomTransactionAmount(user_id, transactionType, productsType);
    }

}
