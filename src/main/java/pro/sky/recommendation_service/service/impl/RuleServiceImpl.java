package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.dto.RecommendationRuleDTO;
import pro.sky.recommendation_service.entity.RecommendationRule;
import pro.sky.recommendation_service.entity.enums.Queries;
import pro.sky.recommendation_service.repository.RulesRepository;
import pro.sky.recommendation_service.service.RulesService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RulesService {

    private final RulesRepository rulesRepository;

    @Override
    public List<RecommendationRule> getAllRules() {
        return rulesRepository.getAllRules();
    }

    @Override
    public void createRule(RecommendationRuleDTO rule) {
        checkArguments(rule);
        rulesRepository.addRule(rule);
    }
//
//    @Override
//    public void deleteRule(UUID id) {
//        rulesRepository.deleteRuleById(id);
//    }

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
     * @param query строка для проверки
     */
    public boolean isQueryValid(Queries query) {
        try {
            Queries.valueOf(query.name());
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
        if (isQueryValid(rule.getQuery())) {
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
