package pro.sky.recommendation_service.component.recommendationRule;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.repository.RecommendationsRepository;

import java.util.Optional;
import java.util.UUID;

@Component("SimpleCreditRule")
@RequiredArgsConstructor
public class SimpleCreditRule implements RecommendationRuleSet {

    // Описание продукта рекомендации
    private final String NAME = "Простой кредит";
    private final UUID ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private final String TEXT = """
            Откройте мир выгодных кредитов с нами!
            Ищете способ быстро и без лишних хлопот получить нужную сумму?
            Тогда наш выгодный кредит — именно то, что вам нужно!
            Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.
            Почему выбирают нас:
            Быстрое рассмотрение заявки.
            Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.
            Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.
            Широкий выбор кредитных продуктов.
            Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.
            Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!""";

    // Поле констант
    String PRODUCT_TYPE_DEBIT = "DEBIT"; // onlyInUpperCase
    String PRODUCT_TYPE_CREDIT = "CREDIT"; // onlyInUpperCase
    String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT"; // onlyInUpperCase
    String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW"; // onlyInUpperCase
    int TRANSACTION_CONDITION = 100_000;

    private final Logger logger = LoggerFactory.getLogger(SimpleCreditRule.class);

    private final RecommendationsRepository recommendationsRepository;

    @Override
    public Optional<RecommendationDTO> checkRecommendation(UUID user_id) {

        logger.info("Starting checking {} recommendation for user_id: {}", NAME, user_id);
        if (!hasCreditProduct(user_id)
                && hasPositiveDebitBalance(user_id)
                && hasDebitWithdrawCondition(user_id)
        ) {
            logger.info("Found {} recommendation for user_id: {}", NAME, user_id);
            return Optional.of(
                    new RecommendationDTO(NAME, ID, TEXT));
        }
        logger.info("Not Found {} recommendation for user_id: {}", NAME, user_id);
        return Optional.empty();
    }

    // Рефакторинг условий с помощью дополнительных методов
    public boolean hasCreditProduct(UUID user_id) {
        return recommendationsRepository.isProductExists(user_id, PRODUCT_TYPE_CREDIT);
    }

    public boolean hasPositiveDebitBalance(UUID user_id) {
        return recommendationsRepository.getTransactionAmount
                (user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_DEPOSIT)
                > recommendationsRepository.getTransactionAmount
                (user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_WITHDRAW);
    }

    public boolean hasDebitWithdrawCondition(UUID user_id) {
        return recommendationsRepository.getTransactionAmount(user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_WITHDRAW)
                > TRANSACTION_CONDITION;
    }

}
