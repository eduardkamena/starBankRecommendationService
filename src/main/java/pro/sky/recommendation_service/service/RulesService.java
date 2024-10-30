package pro.sky.recommendation_service.service;

import pro.sky.recommendation_service.dto.RulesDTO;

public interface RulesService {

    RulesDTO createRule(RulesDTO rulesDTO);

    void deleteRule(String query);

}
