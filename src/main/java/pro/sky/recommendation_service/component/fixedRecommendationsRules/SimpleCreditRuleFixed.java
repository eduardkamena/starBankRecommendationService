package pro.sky.recommendation_service.component.fixedRecommendationsRules;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.component.FixedRecommendationsRulesSet;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.repository.FixedRecommendationsRepository;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("simpleCreditRule")
@RequiredArgsConstructor
public class SimpleCreditRuleFixed implements FixedRecommendationsRulesSet {

    // Идентификатор продукта из БД (product_id)
    private final UUID ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");

    // Название продукта рекомендации из БД (product_name)
    String NAME = "Простой кредит";

    // Поле констант условий выполнения клиентом для предоставления продукта рекомендации
    String PRODUCT_TYPE_DEBIT = "DEBIT"; // onlyInUpperCase
    String PRODUCT_TYPE_CREDIT = "CREDIT"; // onlyInUpperCase
    String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT"; // onlyInUpperCase
    String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW"; // onlyInUpperCase
    int TRANSACTION_CONDITION = 100_000;

    private final Logger logger = LoggerFactory.getLogger(SimpleCreditRuleFixed.class);

    private final FixedRecommendationsRepository fixedRecommendationsRepository;
    private final ProductRecommendationsService productRecommendationsService;

    @Override
    @Cacheable(cacheNames = "fixedRecommendations", keyGenerator = "customKeyGenerator")
    public Optional<List<ProductRecommendationsDTO>> checkRecommendation(UUID userId) {

        logger.info("Starting checking {} recommendation for userId: {}", NAME, userId);
        if (!hasCreditProduct(userId)
                && hasPositiveDebitBalance(userId)
                && hasDebitWithdrawCondition(userId)
        ) {
            logger.info("Found {} recommendation for userId: {}", NAME, userId);
            return Optional.of(productRecommendationsService.getRecommendationProduct(ID));
        }
        logger.info("Not Found {} recommendation for userId: {}", NAME, userId);
        return Optional.empty();
    }

    // Условия выполнения клиентом для предоставления продукта рекомендации
    public boolean hasCreditProduct(UUID userId) {
        return fixedRecommendationsRepository.isProductExists(userId, PRODUCT_TYPE_CREDIT);
    }

    public boolean hasPositiveDebitBalance(UUID userId) {
        return fixedRecommendationsRepository.getTransactionAmount
                (userId, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_DEPOSIT)
                > fixedRecommendationsRepository.getTransactionAmount
                (userId, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_WITHDRAW);
    }

    public boolean hasDebitWithdrawCondition(UUID userId) {
        return fixedRecommendationsRepository.getTransactionAmount(userId, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_WITHDRAW)
                > TRANSACTION_CONDITION;
    }

}
