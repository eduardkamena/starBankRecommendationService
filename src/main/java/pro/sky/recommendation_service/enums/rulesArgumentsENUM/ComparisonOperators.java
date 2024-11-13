package pro.sky.recommendation_service.enums.rulesArgumentsENUM;

import lombok.Getter;

/**
 * Перечисление возможных значений для аргумента правил динамической рекомендации
 */
@Getter
public enum ComparisonOperators {

    A(">"),
    B("<"),
    C(">="),
    D("<="),
    E("=");

    private final String operatorVal;

    ComparisonOperators(String operatorVal) {
        this.operatorVal = operatorVal;
    }

}
