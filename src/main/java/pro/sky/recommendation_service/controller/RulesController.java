package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.recommendation_service.entity.Rule;
import pro.sky.recommendation_service.service.RulesService;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
@Tag(
        name = "Контроллер правил",
        description = "Выполняет действия с правилами для подбора рекомендаций"
)
public class RulesController {

    private final RulesService rulesService;

    @GetMapping(path = "/list")
    @Operation(
            summary = "Получение всех правил",
            description = "Позволяет поулчить список всех возможных правил для подбора рекомендаций"
    )
    public List<Rule> getAllRules() throws SQLException {
        return rulesService.getAllRules();
    }

    @PostMapping("/add")
    @Operation(
            summary = "Добавление правила",
            description = "Позволяет добавлять правило для подбора рекомендаций"
    )
    public ResponseEntity<Rule> createRule(@RequestBody Rule rule) {
        try{
            Rule createdRule = rulesService.createRule(rule);
            return new ResponseEntity<>(createdRule, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Удаление првила",
            description = "Позволяет удалить правило из списка"
    )
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        rulesService.deleteRule(id);
        return ResponseEntity.ok().build();
    }

}
