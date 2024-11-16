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

/**
 * Контроллер для работы с динамическими рекомендациями
 */
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

    /**
     * POST-Метод создания в БД записи рекомендации продукта
     * <p>
     *     {@link DynamicRulesRecommendationsService Сервис} по работе с динамическими рекомендациями
     *
     * @param recommendation принимаемый JSON в теле запроса
     * @return 200 - запись создана в БД
     *      <p>400 - при неудаче
     */
    @PostMapping
    @Operation(
            summary = "Создание динамической рекомендации",
            description = "Позволяет создать динамическую рекомендации продукта")
    public ResponseEntity<Object> createDynamicRecommendation(
            @Parameter(description = "Динамическая рекомендация")
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

    /**
     * GET-Метод получения записи рекомендации из БД
     *
     * @param ruleId идентификатор динамической рекомендации
     * @return 200 - JSON динамической рекомендации
     *      <p>400 - рекомендация с таким ID не найдена
     */
    @GetMapping(path = "/{ruleId}")
    @Operation(
            summary = "Получение динамической рекомендации",
            description = "Позволяет получить динамическую рекомендации продукта")
    public ResponseEntity<Object> getDynamicRecommendation(
            @Parameter(description = "ID динамической рекомендации")
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

    /**
     * GET-Метод получения всех записей рекомендаций из БД
     *
     * @return 200 - JSON динамических рекомендаций
     *      <p>400 - рекомендация в БД не найдено
     */
    @GetMapping(path = "/allRules")
    @Operation(
            summary = "Получение всех динамических рекомендаций",
            description = "Позволяет получить все динамические рекомендации продуктов")
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

    /**
     * DELETE-Метод удаления записи рекомендации из БД
     *
     * @param ruleId идентификатор динамической рекомендации
     * @return 200 с телом No Content
     *      <p>400 - рекомендация с таким ID не найдена
     */
    @DeleteMapping(path = "/{ruleId}")
    @Operation(
            summary = "Удаление динамической рекомендации",
            description = "Позволяет удалить динамическую рекомендацию продукта")
    public ResponseEntity<Object> deleteDynamicRecommendation(
            @Parameter(description = "ID динамической рекомендации")
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

    /**
     * GET-Метод получения статистики срабатывания рекомендаций
     * <p>
     * {@link pro.sky.recommendation_service.service.impl.StatsServiceImpl Сервис} статистики динамических рекомендаций
     *
     * @return JSON представления карты срабатывания
     */
    @GetMapping(path = "/stats")
    @Operation(
            summary = "Статистика срабатывания динамических рекомендаций",
            description = "Позволяет получить статистику срабатываний динамических правил рекомендаций продукта")
    public ResponseEntity<Map<String, List<Map<String, ? extends Serializable>>>> getAllStatsCount() {

        logger.info("Receiving a request for recommendations counter stats");

        List<Map<String, ? extends Serializable>> mappingStatsCount = statsService.getAllStatsCount();

        logger.info("Outputting in @Controller recommendations counter stats");
        return ResponseEntity.ok(Map.of("stats", mappingStatsCount));
    }

}
