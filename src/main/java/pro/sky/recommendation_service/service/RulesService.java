package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.entity.Rule;

import java.util.List;
import java.util.UUID;

public interface RulesService {

    List<Rule> getAllRules();
    Rule createRule(Rule rule);
    void deleteRule(UUID id);

}
