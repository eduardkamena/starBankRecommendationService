package pro.sky.recommendation_service.component.fixedRecommendationsRules;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.component.FixedRecommendationsRulesSet;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.repository.FixedRecommendationsRepository;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("SimpleCreditRule")
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
    public Optional<List<ProductRecommendationsDTO>> checkRecommendation(UUID user_id) {

        logger.info("Starting checking {} recommendation for user_id: {}", NAME, user_id);
        if (!hasCreditProduct(user_id)
                && hasPositiveDebitBalance(user_id)
                && hasDebitWithdrawCondition(user_id)
        ) {
            logger.info("Found {} recommendation for user_id: {}", NAME, user_id);
            return Optional.of(productRecommendationsService.getRecommendationProduct(ID));
        }
        logger.info("Not Found {} recommendation for user_id: {}", NAME, user_id);
        return Optional.empty();
    }

    // Условия выполнения клиентом для предоставления продукта рекомендации
    public boolean hasCreditProduct(UUID user_id) {
        return fixedRecommendationsRepository.isProductExists(user_id, PRODUCT_TYPE_CREDIT);
    }

    public boolean hasPositiveDebitBalance(UUID user_id) {
        return fixedRecommendationsRepository.getTransactionAmount
                (user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_DEPOSIT)
                > fixedRecommendationsRepository.getTransactionAmount
                (user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_WITHDRAW);
    }

    public boolean hasDebitWithdrawCondition(UUID user_id) {
        return fixedRecommendationsRepository.getTransactionAmount(user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_WITHDRAW)
                > TRANSACTION_CONDITION;
    }

}
