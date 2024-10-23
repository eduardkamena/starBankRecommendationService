package pro.sky.recommendation_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.recommendation_service.entity.Rule;
import pro.sky.recommendation_service.repository.RulesRepository;
import pro.sky.recommendation_service.service.RulesService;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RulesService {

    private final RulesRepository rulesRepository;

    @Override
    public List<Rule> getAllRules() throws SQLException {
        return rulesRepository.getAllRules();
    }

    @Override
    public Rule createRule(Rule rule) {
        return null;
    }

    @Override
    public void deleteRule(String id) {

    }
}
