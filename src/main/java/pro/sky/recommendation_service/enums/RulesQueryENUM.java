package pro.sky.recommendation_service.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Перечисление возможных значений для запроса правил динамической рекомендации
 */
@JsonDeserialize(using = RulesQueryENUMDeserializer.class)
public enum RulesQueryENUM {

    USER_OF,
    ACTIVE_USER_OF,
    TRANSACTION_SUM_COMPARE,
    TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW

}
