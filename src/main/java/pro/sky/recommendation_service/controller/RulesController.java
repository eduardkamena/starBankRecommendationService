package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pro.sky.recommendation_service.dto.RulesDTO;
import pro.sky.recommendation_service.service.RulesService;

import java.util.List;
import java.util.Optional;

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
    public RulesDTO createRule(@RequestBody RulesDTO rules) {
        return rulesService.createRule(rules);

    }

    @DeleteMapping
    public void deleteRule(@RequestBody String query) {
        rulesService.deleteRule(query);

    }

    @GetMapping
    public Optional<List<RulesDTO>> readAllRules() {
        return rulesService.readAllRules();
    }

}
