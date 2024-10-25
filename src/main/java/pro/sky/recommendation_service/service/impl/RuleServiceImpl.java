package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.entity.Rule;
import pro.sky.recommendation_service.repository.RulesRepository;
import pro.sky.recommendation_service.service.RulesService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RulesService {

    private final RulesRepository rulesRepository;

    @Override
    public List<Rule> getAllRules() {
        return rulesRepository.getAllRules();
    }

    @Override
    public Rule createRule(Rule rule) {
        try {
            checkArguments(rule);
            rulesRepository.addRule(rule);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
        return rule;
    }

    @Override
    public void deleteRule(UUID id) {
        rulesRepository.deleteRuleById(id);
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
     * Проверяет передаваемые значения аргументов
     *
     * @param rule правило, чьи аргументы будут проверяться на соответствие
     * @throws IllegalArgumentException если аргументы не соответствуют или отсутствуют вовсе
     */
    public void checkArguments(Rule rule) {
        String[] arguments = rule.getArguments();
        if (rule.getQuery().isEmpty() || rule.getArguments().length == 0) {
            throw new IllegalArgumentException("Rule must have at least one argument");
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
