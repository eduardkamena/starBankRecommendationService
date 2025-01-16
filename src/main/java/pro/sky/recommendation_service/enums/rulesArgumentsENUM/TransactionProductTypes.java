package pro.sky.recommendation_service.enums.rulesArgumentsENUM;

/**
 * Перечисление возможных типов продуктов и транзакций для аргументов правил динамической рекомендации.
 * <p>
 * Используется для указания типов продуктов и транзакций в правилах рекомендаций.
 */
public enum TransactionProductTypes {

    // Product type
    DEBIT,
    CREDIT,
    INVEST,
    SAVING,

    // Transaction type
    WITHDRAW,
    DEPOSIT,

}
