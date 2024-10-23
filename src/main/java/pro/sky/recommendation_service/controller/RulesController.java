package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.recommendation_service.entity.Rule;
import pro.sky.recommendation_service.service.RulesService;

import java.sql.SQLException;
import java.util.List;

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
    public ResponseEntity<Object> getAllRules(){
        List<Rule> rules;
        try {
            rules = rulesService.getAllRules();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(rules);
    }

    @PostMapping
    @Operation(
            summary = "Добавление правила",
            description = "Позволяет добавлять правило для подбора рекомендаций"
    )
    public ResponseEntity<Void> createRule(@RequestBody Rule rule) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Удаление првила",
            description = "Позволяет удалить правило из списка"
    )
    public ResponseEntity<Void> deleteRule(@RequestParam(value = "ruleId") Integer ruleId) {

        //работа с репозиторием
        //return верный

        return ResponseEntity.noContent().build();
    }

}
