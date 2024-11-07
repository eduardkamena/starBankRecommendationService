package pro.sky.recommendation_service.controller;

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
public class RuleStatsController {
    private final RuleExecutionStatsService statsService;

    public RuleStatsController(RuleExecutionStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, List<Map<String,? extends Number>>>> getRuleStats() {

        List<Map<String, ? extends Number>> statsList = statsService.getAllRuleExecutionStats().stream()
                .map(stats -> {
                    Long ruleId = stats.getRuleId() != null ? stats.getRuleId() : 0L;
                    int count = stats.getCount();

                    return Map.of("rule_id", ruleId, "count", count);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("stats", statsList));
    }
}

