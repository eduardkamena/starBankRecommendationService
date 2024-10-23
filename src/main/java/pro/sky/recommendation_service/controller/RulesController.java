package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
@Tag(
        name = "Контроллер правил",
        description = "Выполняет действия с правилами для подбора рекомендаций"
)
public class RulesController {

    @GetMapping(path = "/list")
    @Operation(
            summary = "Получение всех правил",
            description = "Позволяет поулчить список всех возможных правил для подбора рекомендаций"
    )
    public ResponseEntity<Object> getAllRules(){
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(
            summary = "Добавление правила",
            description = "Позволяет добавлять правило для подбора рекомендаций"
    )
    public ResponseEntity<?> createRule() {
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
