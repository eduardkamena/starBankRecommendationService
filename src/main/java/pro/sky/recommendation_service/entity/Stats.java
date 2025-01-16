package pro.sky.recommendation_service.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Класс, представляющий сущность счетчика срабатывания рекомендаций.
 * <p>
 * Используется для хранения данных о количестве срабатываний рекомендации в базе данных.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "@Entity счетчика срабатывания рекомендаций")
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Идентификатор счетчика рекомендации в БД (primary key)")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Schema(description = "Количество срабатывания рекомендаций")
    @Column(name = "count", columnDefinition = "INTEGER", nullable = false)
    private Integer count;

    @OneToOne
    @JoinColumn(name = "recommendations_id", nullable = false)
    private Recommendations recommendations;

    /**
     * Конструктор для создания объекта Stats с указанием количества срабатываний.
     *
     * @param count количество срабатываний
     */
    public Stats(Integer count) {
        this.count = count;
    }

}
