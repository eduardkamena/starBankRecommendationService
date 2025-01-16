package pro.sky.recommendation_service.enums.rulesArgumentsENUM;

import lombok.Getter;

/**
 * Перечисление возможных значений для сравнения суммы транзакций в правилах динамической рекомендации.
 * <p>
 * Используется для указания пороговых значений суммы транзакций.
 */
@Getter
public enum SumCompare {

    A(1_000),
    B(50_000),
    C(100_000);

    private final int sumVal;

    /**
     * Конструктор для создания элемента перечисления с указанием суммы.
     *
     * @param sumVal целочисленное значение суммы
     */
    SumCompare(int sumVal) {
        this.sumVal = sumVal;
    }

}
