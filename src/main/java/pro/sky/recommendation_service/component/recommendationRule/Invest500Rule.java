package pro.sky.recommendation_service.component.recommendationRule;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.repository.RecommendationsRepository;
import pro.sky.recommendation_service.repository.TransactionsRepository;

import java.util.Optional;
import java.util.UUID;

@Component("Invest500Rule")
@RequiredArgsConstructor
public class Invest500Rule implements RecommendationRuleSet {

    // Описание продукта рекомендации
    private final String NAME = "Invest 500";
    private final UUID ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");

    // Поле констант
    String PRODUCT_TYPE_DEBIT = "DEBIT"; // onlyInUpperCase
    String PRODUCT_TYPE_INVEST = "INVEST"; // onlyInUpperCase
    String PRODUCT_TYPE_SAVING = "SAVING"; // onlyInUpperCase
    String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT"; // onlyInUpperCase
    int TRANSACTION_CONDITION = 1000;

    private final Logger logger = LoggerFactory.getLogger(Invest500Rule.class);

    private final TransactionsRepository transactionsRepository;
    private final RecommendationsRepository recommendationsRepository;

    @Override
    public Optional<RecommendationDTO> checkRecommendation(UUID user_id) {

        logger.info("Starting checking {} recommendation for user_id: {}", NAME, user_id);
        if (hasDebitProduct(user_id)
                && !hasInvestProduct(user_id)
                && hasSavingDepositCondition(user_id)
        ) {
            logger.info("Found {} recommendation for user_id: {}", NAME, user_id);
            return recommendationsRepository.getProductDescription(ID);

        }
        logger.info("Not Found {} recommendation for user_id: {}", NAME, user_id);
        return Optional.empty();
    }

    // Рефакторинг условий с помощью дополнительных методов
    public boolean hasDebitProduct(UUID user_id) {
        return transactionsRepository.isProductExists(user_id, PRODUCT_TYPE_DEBIT);
    }

    public boolean hasInvestProduct(UUID user_id) {
        return transactionsRepository.isProductExists(user_id, PRODUCT_TYPE_INVEST);
    }

    public boolean hasSavingDepositCondition(UUID user_id) {
        return transactionsRepository.getTransactionAmount(user_id, PRODUCT_TYPE_SAVING, TRANSACTION_TYPE_DEPOSIT)
                > TRANSACTION_CONDITION;
    }

}
