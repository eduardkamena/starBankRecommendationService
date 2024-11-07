package pro.sky.recommendation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.recommendation_service.service.RuleExecutionStatsService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/rule")
@Tag(
        name = "Контроллер статистики правил рекомендаций",
        description = "Выполняет действия со статистикой правил рекомендаций")
public class RuleStatsController {

    private final Logger logger = LoggerFactory.getLogger(RuleStatsController.class);

    private final RuleExecutionStatsService statsService;

    public RuleStatsController(RuleExecutionStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    @Operation(
            summary = "Статистика срабатывания правил рекомендаций",
            description = "Позволяет получить статистику срабатываний правил рекомендаций")
    public ResponseEntity<Map<String, List<Map<String, ? extends Number>>>> getRuleStats() {

        logger.info("Receiving a request for counter response statistics");

        List<Map<String, ? extends Number>> statsList = statsService.getAllRuleExecutionStats().stream()
                .map(stats -> {
                    Long ruleId = stats.getRuleId() != null ? stats.getRuleId() : 0L;
                    int count = stats.getCount();

                    return Map.of("rule_id", ruleId, "count", count);
                })
                .collect(Collectors.toList());

        logger.info("Outputting in @Controller counter response statistics");

        return ResponseEntity.ok(Map.of("stats", statsList));
    }

}
