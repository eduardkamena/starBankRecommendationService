package pro.sky.recommendation_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "@Entity объекта запроса для правила рекомендации")
public class Rules {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Идентификатор объекта запроса для правила рекомендации в БД (primary key)")
    private UUID id;

    @Schema(description = "Название объекта запроса для правила рекомендации")
    private String query;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "rules_arguments", joinColumns = @JoinColumn(name = "rules_id"))
    @Column(name = "argument")
    @OrderColumn(name = "argument_index")
    @Schema(description = "Аргументы объекта запроса для правила рекомендации")
    private List<String> arguments;

    @Schema(description = "Соответствие объекта запроса для правила рекомендации)")
    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "recommendations_id")
    private Recommendations recommendations;

}
