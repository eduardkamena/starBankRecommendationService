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
@Schema(description = "Сущность объекта запроса")
public class Rules {

    @Id
    @JsonIgnore
    @Schema(description = "ID объекта запроса")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Schema(description = "Название объекта запроса")
    private String query;

    @Schema(description = "Аргументы объекта запроса")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "rules_arguments", joinColumns = @JoinColumn(name = "rules_id"))
    @Column(name = "argument")
    @OrderColumn(name = "argument_index")
    private List<String> arguments;

    @Schema(description = "Подтверждение объекта запроса")
    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "recommendations_id")
    private Recommendations recommendations;

}
