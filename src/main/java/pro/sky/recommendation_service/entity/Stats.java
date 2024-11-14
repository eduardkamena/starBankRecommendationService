package pro.sky.recommendation_service.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "@Entity счетчика срабатывания рекомендаций")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Идентификатор счетчика рекомендации в БД (primary key)")
    private UUID id;

    @Column(columnDefinition = "INTEGER", nullable = false)
    @Schema(description = "Количество срабатывания рекомендаций")
    private Integer count;

    @OneToOne
    @JoinColumn(name = "recommendations_id", nullable = false)
    private Recommendations recommendations;

    public Stats(Integer count) {
        this.count = count;
    }

}
