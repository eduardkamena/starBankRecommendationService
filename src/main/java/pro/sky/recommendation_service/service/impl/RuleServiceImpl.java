package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RecommendationRuleDTO;
import pro.sky.recommendation_service.entity.RecommendationRule;
import pro.sky.recommendation_service.entity.enums.Queries;
import pro.sky.recommendation_service.repository.RulesRepository;
import pro.sky.recommendation_service.service.RulesService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RulesService {

    private final RulesRepository rulesRepository;

    @Override
    public List<RecommendationRule> getAllRules() {
        return rulesRepository.getAllRules();
    }

    @Override
    public void createSimpleRule(RecommendationRuleDTO rule) {
        checkArguments(rule);
        rulesRepository.addSimpleRule(rule);
    }

    @Override
    public void createRuleFromRecommendation(UUID id, RecommendationRuleDTO rule) {
        checkArguments(rule);
        rulesRepository.addRecommendationRule(id, rule);
    }

    /**
     * Метод проверки, позволяющий узнать является ли строка положительным числом в пределах Integer
     *
     * @param str строка для проверки
     * @return true - если число положительное И в пределах Integer
     *         false - если число отрицательное ИЛИ за пределами Integer
     */
    public boolean isPositiveNumber(String str) {
        try {
            return Integer.parseInt(str) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Метод проверки, позволяющий узнать есть ли query в enum Queries
     *
     * @param rule сущность, запрос которой мы проверяем
     */
    public boolean isQueryValid(RecommendationRuleDTO rule) {
        try {
            Queries.valueOf(rule.getQuery().toString());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Проверяет передаваемые значения аргументов
     *
     * @param rule правило, чьи аргументы будут проверяться на соответствие
     * @throws IllegalArgumentException если аргументы/запрос не соответствуют или отсутствуют вовсе
     */
    public void checkArguments(RecommendationRuleDTO rule) {
        List<String> arguments = rule.getArguments();
        if (!isQueryValid(rule)) {
            throw new IllegalArgumentException("Rule must pass a query from a list of possible");
        }

        for (String argument : arguments) {
            if (isPositiveNumber(argument)
            ||argument.equals("DEBIT")
            || argument.equals("CREDIT")
            || argument.equals("INVEST")
            || argument.equals("DEPOSIT")
            || argument.equals(">")
            || argument.equals(">=")
            || argument.equals("<")
            || argument.equals("<=")
            || argument.equals("=")) {
            } else {
                throw new IllegalArgumentException("Rule must have at least one argument");
            }
        }
    }


}
