package pro.sky.recommendation_service.component.recommendationRule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.sky.recommendation_service.component.RecommendationRuleSet;
import pro.sky.recommendation_service.dto.RecommendationDTO;
import pro.sky.recommendation_service.repository.RecommendationsRepository;

import java.util.Optional;
import java.util.UUID;

@Component("TopSavingRule")
@RequiredArgsConstructor
public class TopSavingRule implements RecommendationRuleSet {

    // Описание продукта рекомендации
    private final String NAME = "Top Saving";
    private final UUID ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private final String TEXT = """
            Откройте свою собственную «Копилку» с нашим банком!
            «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели.
            Больше никаких забытых чеков и потерянных квитанций — всё под контролем!
            Преимущества «Копилки»:
            Накопление средств на конкретные цели.
            Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.
            Прозрачность и контроль.
            Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.
            Безопасность и надежность.
            Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.
            Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!""";

    // Поле констант
    String PRODUCT_TYPE_DEBIT = "DEBIT"; // onlyInUpperCase
    String PRODUCT_TYPE_SAVING = "SAVING"; // onlyInUpperCase
    String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT"; // onlyInUpperCase
    String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW"; // onlyInUpperCase
    int TRANSACTION_CONDITION = 50_000;

    private final RecommendationsRepository recommendationsRepository;

    @Override
    public Optional<RecommendationDTO> checkRecommendation(UUID user_id) {

        if (hasDebitProduct(user_id)
                && (hasDebitDepositCondition(user_id) || hasSavingDepositCondition(user_id))
                && hasPositiveDebitBalance(user_id)
        ) {
            return Optional.of(
                    new RecommendationDTO(NAME, ID, TEXT));
        }
        return Optional.empty();
    }

    // Рефакторинг условий с помощью дополнительных методов
    public boolean hasDebitProduct(UUID user_id) {
        return recommendationsRepository.isProductExists(user_id, PRODUCT_TYPE_DEBIT);
    }

    public boolean hasDebitDepositCondition(UUID user_id) {
        return recommendationsRepository.getTransactionAmount(user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_DEPOSIT)
                >= TRANSACTION_CONDITION;
    }

    public boolean hasSavingDepositCondition(UUID user_id) {
        return recommendationsRepository.getTransactionAmount(user_id, PRODUCT_TYPE_SAVING, TRANSACTION_TYPE_DEPOSIT)
                >= TRANSACTION_CONDITION;
    }

    public boolean hasPositiveDebitBalance(UUID user_id) {
        return recommendationsRepository.getTransactionAmount
                (user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_DEPOSIT)
                > recommendationsRepository.getTransactionAmount
                (user_id, PRODUCT_TYPE_DEBIT, TRANSACTION_TYPE_WITHDRAW);
    }

}
