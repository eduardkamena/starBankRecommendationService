package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.recommendation_service.dto.RecommendationsDTO;
import pro.sky.recommendation_service.entity.Recommendations;
import pro.sky.recommendation_service.exception.*;
import pro.sky.recommendation_service.service.DynamicRulesRecommendationsService;
import pro.sky.recommendation_service.service.StatsService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/rule")
@Tag(
        name = "Контроллер динамических правил рекомендаций",
        description = "Выполняет действия с динамическими правилами рекомендаций")
public class DynamicRulesRecommendationsController {

    private final Logger logger = LoggerFactory.getLogger(DynamicRulesRecommendationsController.class);

    private final DynamicRulesRecommendationsService dynamicRulesRecommendationsService;
    private final StatsService statsService;

    public DynamicRulesRecommendationsController(DynamicRulesRecommendationsService dynamicRulesRecommendationsService, StatsService statsService) {
        this.dynamicRulesRecommendationsService = dynamicRulesRecommendationsService;
        this.statsService = statsService;
    }

    @PostMapping
    @Operation(
            summary = "Создание динамического правила рекомендации",
            description = "Позволяет создать динамическое правило рекомендации")
    public ResponseEntity<Object> createDynamicRecommendation(
            @Parameter(description = "Динамическое правило рекомендации")
            @RequestBody RecommendationsDTO recommendation) {

        logger.info("Received request for creating dynamic rule recommendation: {}", recommendation);

        try {
            Recommendations createdRecommendation = dynamicRulesRecommendationsService.createDynamicRuleRecommendation(recommendation);
            logger.info("Successfully created dynamic rule recommendation: {}", recommendation);
            return ResponseEntity.ok(createdRecommendation);

        } catch (IllegalArgumentException | NullOrEmptyException e) {

            logger.error("Error creating dynamic rule recommendation: {}", recommendation, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AppError(HttpStatus.BAD_REQUEST.value(),
                            "Dynamic rule recommendation cannot be created due to an Exception"));
        }
    }

    @GetMapping(path = "/{ruleId}")
    @Operation(
            summary = "Получение динамического правила рекомендации",
            description = "Позволяет получить динамическое правило рекомендации")
    public ResponseEntity<Object> getDynamicRecommendation(
            @Parameter(description = "Идентификатор динамического правила рекомендации")
            @PathVariable UUID ruleId) {

        logger.info("Received request for getting dynamic rule recommendation for ruleId: {}", ruleId);

        try {
            Optional<RecommendationsDTO> foundRecommendation = dynamicRulesRecommendationsService.getDynamicRuleRecommendation(ruleId);
            logger.info("Outputting in @Controller dynamic rule recommendation for ruleId: {}", ruleId);
            return ResponseEntity.ok(foundRecommendation);

        } catch (RuleNotFoundException e) {

            logger.error("Error Outputting in @Controller dynamic rule recommendation for ruleId: {}", ruleId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Dynamic rule recommendation with UUID " + ruleId + " not found in database"));
        }
    }

    @GetMapping(path = "/allRules")
    @Operation(
            summary = "Получение всех динамических правил рекомендаций",
            description = "Позволяет получить все динамические правила рекомендаций")
    public ResponseEntity<Object> getAllDynamicRecommendations() {

        logger.info("Received request for getting all dynamic rules recommendations");

        try {
            List<RecommendationsDTO> foundAllRecommendations = dynamicRulesRecommendationsService.getAllDynamicRulesRecommendations();
            logger.info("Outputting in @Controller all dynamic rules recommendations");
            return ResponseEntity.ok(foundAllRecommendations);

        } catch (RuleNotFoundException e) {

            logger.error("Error Outputting in @Controller all dynamic rules recommendations", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "No Dynamic rule recommendation was found in database"));
        }
    }

    @DeleteMapping(path = "/{ruleId}")
    @Operation(
            summary = "Удаление динамического правила рекомендации",
            description = "Позволяет удалить динамическое правило рекомендации")
    public ResponseEntity<Object> deleteDynamicRecommendation(
            @Parameter(description = "Идентификатор динамического правила рекомендации")
            @PathVariable UUID ruleId) {

        logger.info("Received request for deleting dynamic rule recommendation for ruleId: {}", ruleId);

        try {
            dynamicRulesRecommendationsService.deleteDynamicRuleRecommendation(ruleId);
            logger.info("Successfully deleted dynamic rule recommendation for ruleId: {}", ruleId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new AppError(HttpStatus.NO_CONTENT.value(),
                            "No Content"));

        } catch (RuleNotFoundException e) {

            logger.error("Error deleting dynamic rule recommendation for ruleId: {}", ruleId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new AppError(HttpStatus.NOT_FOUND.value(),
                            "Dynamic rule recommendation with UUID " + ruleId + " not found in database"));
        }

    }

    @GetMapping(path = "/stats")
    @Operation(
            summary = "Статистика срабатывания динамических правил рекомендаций",
            description = "Позволяет получить статистику срабатываний динамических правил рекомендаций")
    public ResponseEntity<Map<String, List<Map<String, ? extends Serializable>>>> getAllStatsCount() {

        logger.info("Receiving a request for recommendations counter stats");

        List<Map<String, ? extends Serializable>> mappingStatsCount = statsService.getAllStatsCount();

        logger.info("Outputting in @Controller recommendations counter stats");
        return ResponseEntity.ok(Map.of("stats", mappingStatsCount));
    }

}
