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

/**
 * Класс, реализующий фиксированное правило рекомендации "Top Saving".
 * <p>
 * Это правило проверяет, имеет ли пользователь дебетовый продукт, выполняет условие по сумме транзакций
 * для дебетового или сберегательного продукта и имеет положительный баланс по дебетовому продукту.
 */
@Component("topSavingRule")
@RequiredArgsConstructor
public class TopSavingRuleFixed implements FixedRecommendationsRulesSet {

    // Идентификатор продукта из БД (product_id)
    private final UUID ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");

    // Название продукта рекомендации из БД (product_name)
    String NAME = "Top Saving";

    // Поле констант условий выполнения клиентом для предоставления продукта рекомендации
    String PRODUCT_TYPE_DEBIT = "DEBIT"; // onlyInUpperCase
    String PRODUCT_TYPE_SAVING = "SAVING"; // onlyInUpperCase
    String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT"; // onlyInUpperCase
    String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW"; // onlyInUpperCase
    int TRANSACTION_CONDITION = 50_000;

    private final Logger logger = LoggerFactory.getLogger(TopSavingRuleFixed.class);

    private final FixedRecommendationsRepository fixedRecommendationsRepository;
    private final ProductRecommendationsService productRecommendationsService;

    /**
     * Проверка выполнения условий для рекомендации "Top Saving".
     *
     * @param userId идентификатор пользователя
     * @return список рекомендаций, если условия выполнены, иначе пустой Optional
     */
    @Override
    @Cacheable(cacheNames = "fixedRecommendations", keyGenerator = "customKeyGenerator")
    public Optional<List<ProductRecommendationsDTO>> checkRecommendation(UUID userId) {

        logger.info("Starting checking {} recommendation for userId: {}", NAME, userId);
        if (hasDebitProduct(userId)
                && (hasDebitDepositCondition(userId) || hasSavingDepositCondition(userId))
                && hasPositiveDebitBalance(userId)
        ) {
            logger.info("Found {} recommendation for userId: {}", NAME, userId);
            return Optional.of(productRecommendationsService.getRecommendationProduct(ID));
        }
        logger.info("Not Found {} recommendation for userId: {}", NAME, userId);
        return Optional.empty();
    }

    /**
     * Проверка наличия у пользователя дебетового продукта.
     *
     * @param userId идентификатор пользователя
     * @return true, если у пользователя есть дебетовый продукт, иначе false
     */
    public boolean hasDebitProduct(UUID userId) {
        return fixedRecommendationsRepository.isProductExists(userId, PRODUCT_TYPE_DEBIT);
    }

    /**
     * Проверка выполнения условия по сумме транзакций для дебетового продукта.
     *
     * @param userId идентификатор пользователя
     * @return true, если сумма транзакций превышает заданное условие, иначе false
     */
    public boolean hasDebitDepositCondition(UUID userId) {
        return fixedRecommendationsRepository.getTransactionAmount(userId, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_DEPOSIT)
                >= TRANSACTION_CONDITION;
    }

    /**
     * Проверка выполнения условия по сумме транзакций для сберегательного продукта.
     *
     * @param userId идентификатор пользователя
     * @return true, если сумма транзакций превышает заданное условие, иначе false
     */
    public boolean hasSavingDepositCondition(UUID userId) {
        return fixedRecommendationsRepository.getTransactionAmount(userId, PRODUCT_TYPE_SAVING, TRANSACTION_TYPE_DEPOSIT)
                >= TRANSACTION_CONDITION;
    }

    /**
     * Проверка положительного баланса по дебетовому продукту.
     *
     * @param userId идентификатор пользователя
     * @return true, если баланс положительный, иначе false
     */
    public boolean hasPositiveDebitBalance(UUID userId) {
        return fixedRecommendationsRepository.getTransactionAmount
                (userId, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_DEPOSIT)
                > fixedRecommendationsRepository.getTransactionAmount
                (userId, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_WITHDRAW);
    }

}
