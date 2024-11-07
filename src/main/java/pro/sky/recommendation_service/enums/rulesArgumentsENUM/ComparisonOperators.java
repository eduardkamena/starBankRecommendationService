package pro.sky.recommendation_service.enums.rulesArgumentsENUM;

import lombok.Getter;

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
