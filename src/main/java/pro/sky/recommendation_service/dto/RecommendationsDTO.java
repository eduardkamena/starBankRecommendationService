package pro.sky.recommendation_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.recommendation_service.entity.Stats;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность рекомендации")
public class RecommendationsDTO {

    @Schema(description = "Идентификатор правила рекомендации в БД")
    private UUID id;

    @Schema(description = "Название рекомендации")
    private String product_name;

    @Schema(description = "Идентификатор рекомендации в продакшн")
    private UUID product_id;

    @Schema(description = "Описание рекомендации")
    private String product_text;

    @Schema(description = "Правила рекомендации")
    private List<RulesDTO> rule;

    @JsonIgnore
    @Schema(description = "Счетчик срабатывания рекомендаций")
    private Stats stats;

}
