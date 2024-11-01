package pro.sky.recommendation_service.component.fixedRecommendationRule;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.component.FixedRecommendationRuleSet;
import pro.sky.recommendation_service.dto.ProductRecommendationsDTO;
import pro.sky.recommendation_service.repository.TransactionsRepository;
import pro.sky.recommendation_service.service.ProductRecommendationsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("Invest500Rule")
@RequiredArgsConstructor
public class Invest500RuleFixed implements FixedRecommendationRuleSet {

    // Идентификатор продукта из БД (product_id)
    private final UUID ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");

    // Название продукта рекомендации из БД (product_name)
    String NAME = "Invest 500";

    // Поле констант условий выполнения клиентом для предоставления продукта рекомендации
    String PRODUCT_TYPE_DEBIT = "DEBIT"; // onlyInUpperCase
    String PRODUCT_TYPE_INVEST = "INVEST"; // onlyInUpperCase
    String PRODUCT_TYPE_SAVING = "SAVING"; // onlyInUpperCase
    String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT"; // onlyInUpperCase
    int TRANSACTION_CONDITION = 1000;

    private final Logger logger = LoggerFactory.getLogger(Invest500RuleFixed.class);

    private final TransactionsRepository transactionsRepository;
    private final ProductRecommendationsService productRecommendationsService;

    @Override
    public Optional<List<ProductRecommendationsDTO>> checkRecommendation(UUID user_id) {

        logger.info("Starting checking {} recommendation for user_id: {}", NAME, user_id);
        if (hasDebitProduct(user_id)
                && !hasInvestProduct(user_id)
                && hasSavingDepositCondition(user_id)
        ) {
            logger.info("Found {} recommendation for user_id: {}", NAME, user_id);
            return Optional.of(productRecommendationsService.getByProductId(ID));

        }
        logger.info("Not Found {} recommendation for user_id: {}", NAME, user_id);
        return Optional.empty();
    }

    // Условия выполнения клиентом для предоставления продукта рекомендации
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
