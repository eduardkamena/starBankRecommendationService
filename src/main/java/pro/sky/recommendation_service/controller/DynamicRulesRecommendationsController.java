package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.exception.AppError;
import pro.sky.recommendation_service.service.DynamicRulesRecommendationsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/rule")
@Tag(
        name = "Контроллер динамических правил рекомендаций",
        description = "Выполняет действия с динамическими правилами рекомендаций")
public class DynamicRulesRecommendationsController {

    private final DynamicRulesRecommendationsService dynamicRulesRecommendationsService;

    public DynamicRulesRecommendationsController(DynamicRulesRecommendationsService dynamicRulesRecommendationsService) {
        this.dynamicRulesRecommendationsService = dynamicRulesRecommendationsService;
    }

    @PostMapping
    public ResponseEntity<Recommendations> createDynamicRecommendation(
            @Parameter(description = "Создание динамического правила рекомендации")
            @RequestBody RecommendationsDTO recommendation) {

        Recommendations createdRecommendation = dynamicRulesRecommendationsService.createDynamicRuleRecommendation(recommendation);

        return ResponseEntity.ok(createdRecommendation);
    }

    @GetMapping(path = "/{rule_id}")
    public ResponseEntity<Optional<RecommendationsDTO>> getDynamicRecommendation(
            @Parameter(description = "Идентификатор динамического правила рекомендации")
            @PathVariable UUID rule_id) {

        Optional<RecommendationsDTO> foundRecommendation = dynamicRulesRecommendationsService.getDynamicRuleRecommendation(rule_id);

        return ResponseEntity.ok(foundRecommendation);
    }

    @DeleteMapping(path = "/{rule_id}")
    public ResponseEntity<Object> deleteDynamicRecommendation(
            @Parameter(description = "Идентификатор динамического правила рекомендации")
            @PathVariable UUID rule_id) {

        dynamicRulesRecommendationsService.deleteDynamicRuleRecommendation(rule_id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new AppError(HttpStatus.NO_CONTENT.value(),
                        "No Content"));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<RecommendationsDTO>> getAllDynamicRecommendations() {

        List<RecommendationsDTO> foundAllRecommendations = dynamicRulesRecommendationsService.getAllDynamicRulesRecommendations();

        return ResponseEntity.ok(foundAllRecommendations);
    }

}
