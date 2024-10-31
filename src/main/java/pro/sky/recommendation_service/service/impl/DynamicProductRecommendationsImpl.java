package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.DynamicRecommendationDTO;
import pro.sky.recommendation_service.dto.RecommendationRuleDTO;
import pro.sky.recommendation_service.entity.DynamicProductRecommendation;
import pro.sky.recommendation_service.entity.RecommendationRule;
import pro.sky.recommendation_service.entity.enums.Queries;
import pro.sky.recommendation_service.repository.DynamicProductRecommendationsRepository;
import pro.sky.recommendation_service.service.DynamicProductRecommendationsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<DynamicProductRecommendation> getAvailableRecommendationsForUser(UUID userId) {

        //Получаем Map вида ID рекомендации, Список правил рекомендации
        Map<UUID, List<RecommendationRule>> rulesMapByRecommendationId =
                dynamicProductRecommendationRepository.getAllRecommendations().stream()
                        .collect(Collectors.toMap(
                                DynamicProductRecommendation::getId,
                                RecommendationRule -> new ArrayList<>(RecommendationRule.getRecommendationRules()),
                                (existingList, newList) -> {
                                    existingList.addAll(newList);
                                    return existingList;
                                }
                        ));

        return getAllDynamicRecommendations().stream()
                .filter(recommendation -> checkRules(userId, rulesMapByRecommendationId.get(recommendation.getId())))
                .collect(Collectors.toList());
    }

    //Каждый IF выполняет подстановку в метод проверки аргументы из переданного правила
    //Метод проверки возвращает Integer 1 или 0 и добавляет это значение к accessRules.
    //Если rules.size() == выполненным проверкам - то добавляем продукт в return
    public boolean checkRules(UUID Id, List<RecommendationRule> list) {
        Integer accessRules = 0;
        list.forEach(
                rule -> {
                    if (Queries.USER_OF.equals(rule.getQuery())) {
                        return;
                    } else if (Queries.ACTIVE_USER_OF.equals(rule.getQuery())) {
                        return;
                    } else if (Queries.TRANSACTION_SUM_COMPARE.equals(rule.getQuery())) {
                        return;
                    } else if (Queries.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW.equals(rule.getQuery())) {
                        return;
                    }
                });
        return list.size() == accessRules;
    }
}
