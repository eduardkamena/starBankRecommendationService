package pro.sky.recommendation_service.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Перечисление возможных запросов для правил динамической рекомендации.
 * <p>
 * Используется для указания типа запроса в правилах рекомендаций.
 */
@JsonDeserialize(using = RulesQueryENUMDeserializer.class)
public enum RulesQueryENUM {

    USER_OF,
    ACTIVE_USER_OF,
    TRANSACTION_SUM_COMPARE,
    TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW

}
