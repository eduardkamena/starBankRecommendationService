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
 * Класс, реализующий фиксированное правило рекомендации "Invest 500".
 * <p>
 * Это правило проверяет, имеет ли пользователь дебетовый продукт, не имеет инвестиционного продукта
 * и выполняет условие по сумме транзакций для сберегательного продукта.
 */
@Component("invest500Rule")
@RequiredArgsConstructor
public class Invest500RuleFixed implements FixedRecommendationsRulesSet {

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

    private final FixedRecommendationsRepository fixedRecommendationsRepository;
    private final ProductRecommendationsService productRecommendationsService;

    /**
     * Проверка выполнения условий для рекомендации "Invest 500".
     *
     * @param userId идентификатор пользователя
     * @return список рекомендаций, если условия выполнены, иначе пустой Optional
     */
    @Override
    @Cacheable(cacheNames = "fixedRecommendations", keyGenerator = "customKeyGenerator")
    public Optional<List<ProductRecommendationsDTO>> checkRecommendation(UUID userId) {

        logger.info("Starting checking {} recommendation for userId: {}", NAME, userId);
        if (hasDebitProduct(userId)
                && !hasInvestProduct(userId)
                && hasSavingDepositCondition(userId)
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
     * Проверка наличия у пользователя инвестиционного продукта.
     *
     * @param userId идентификатор пользователя
     * @return true, если у пользователя есть инвестиционный продукт, иначе false
     */
    public boolean hasInvestProduct(UUID userId) {
        return fixedRecommendationsRepository.isProductExists(userId, PRODUCT_TYPE_INVEST);
    }

    /**
     * Проверка выполнения условия по сумме транзакций для сберегательного продукта.
     *
     * @param userId идентификатор пользователя
     * @return true, если сумма транзакций превышает заданное условие, иначе false
     */
    public boolean hasSavingDepositCondition(UUID userId) {
        return fixedRecommendationsRepository.getTransactionAmount(userId, PRODUCT_TYPE_SAVING, TRANSACTION_TYPE_DEPOSIT)
                > TRANSACTION_CONDITION;
    }

}
