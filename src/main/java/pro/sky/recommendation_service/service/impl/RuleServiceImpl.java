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
        return null;
    }

    @Override
    public void deleteRule(UUID ruleId) {
        rulesRepository.deleteRuleById(ruleId.toString());
    }
}
