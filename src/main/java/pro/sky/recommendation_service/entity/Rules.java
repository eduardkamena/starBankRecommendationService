package pro.sky.recommendation_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.recommendation_service.enums.RulesQueryENUM;

import java.util.List;
import java.util.UUID;

/**
 * Класс, представляющий сущность правила рекомендации.
 * <p>
 * Используется для хранения данных о правиле рекомендации в базе данных.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "@Entity объекта запроса для правила рекомендации")
@Table(name = "rules")
public class Rules {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Идентификатор объекта запроса для правила рекомендации в БД (primary key)")
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Название объекта запроса для правила рекомендации")
    @Column(name = "query", nullable = false)
    private RulesQueryENUM query;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rules_arguments", joinColumns = @JoinColumn(name = "rules_id"))
    @Schema(description = "Аргументы объекта запроса для правила рекомендации")
    @Column(name = "argument", nullable = false)
    @OrderColumn(name = "argument_index")
    private List<String> arguments;

    @Schema(description = "Соответствие объекта запроса для правила рекомендации)")
    @Column(name = "negate", nullable = false)
    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "recommendations_id")
    private Recommendations recommendations;

}
