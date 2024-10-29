package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.service.RulesService;

@RestController
@RequestMapping(path = "/rules")
@Tag(
        name = "Контроллер правил рекомендаций",
        description = "Выполняет действия с правилами рекомендаций")
public class RulesController {

    private final RulesService rulesService;

    public RulesController(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @PostMapping
    public RulesDTO createRules(@RequestBody RulesDTO rules) {
        return rulesService.createRule(rules);

    }
}
