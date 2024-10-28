package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.DynamicRecommendationDTO;
import pro.sky.recommendation_service.dto.RecommendationRuleDTO;
import pro.sky.recommendation_service.entity.DynamicProductRecommendation;
import pro.sky.recommendation_service.repository.DynamicProductRecommendationsRepository;
import pro.sky.recommendation_service.service.DynamicProductRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DynamicProductRecommendationsImpl implements DynamicProductRecommendationsService {

    private final DynamicProductRecommendationsRepository dynamicProductRecommendationRepository;
    private final RuleServiceImpl ruleService;

    public void createDynamicRecommendations(DynamicRecommendationDTO dynamicRecommendationDTO) {
        List<RecommendationRuleDTO> recommendationRules = dynamicRecommendationDTO.getRule();

        UUID createdRecommendationID = dynamicProductRecommendationRepository.createProductRecommendation(dynamicRecommendationDTO);

        recommendationRules.forEach(
                recommendationRuleDTO -> ruleService.createRuleFromRecommendation(createdRecommendationID, recommendationRuleDTO));
    }

    public List<DynamicProductRecommendation> getAllDynamicRecommendations() {
        return dynamicProductRecommendationRepository.getAllRecommendations();
    }

}
