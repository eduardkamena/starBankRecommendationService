package pro.sky.recommendation_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class RuleExecutionStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @Column(name = "count", nullable = false)
    private int count = 0;

    public RuleExecutionStats(Long ruleId, int count) {
        this.ruleId = ruleId;
        this.count = count;
    }

}