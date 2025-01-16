package pro.sky.recommendation_service.enums.rulesArgumentsENUM;

import lombok.Getter;

/**
 * Перечисление возможных операторов сравнения для аргументов правил динамической рекомендации.
 * <p>
 * Используется для указания операторов сравнения в правилах рекомендаций.
 */
@Getter
public enum ComparisonOperators {

    A(">"),
    B("<"),
    C(">="),
    D("<="),
    E("=");

    private final String operatorVal;

    /**
     * Конструктор для создания элемента перечисления с указанием оператора.
     *
     * @param operatorVal строковое значение оператора
     */
    ComparisonOperators(String operatorVal) {
        this.operatorVal = operatorVal;
    }

}
