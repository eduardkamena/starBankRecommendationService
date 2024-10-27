package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.repository.DynamicProductRecommendationsRepository;
import pro.sky.recommendation_service.service.DynamicProductRecommendationsService;

@Service
@RequiredArgsConstructor
public class DynamicProductRecommendationsImp implements DynamicProductRecommendationsService {

    private final DynamicProductRecommendationsRepository dynamicProductRecommendationRepository;

//    public Recommendation createRecommendation(Recommendation recommendation) {
//        dynamicProductRecommendationRepository.createProductRecommendation(recommendation);
//        return recommendation;
//    }

}
