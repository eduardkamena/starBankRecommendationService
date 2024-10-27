package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Сущность рекомендации")
public class DynamicRecommendationDTO {

    @Schema(description = "Название рекомендации")
    private final String product_name;

    @Schema(description = "Идентификатор рекомендации")
    private final UUID product_id;

    @Schema(description = "Описание рекомендации")
    private final String product_text;

    @Schema(description = "Список правил")
    private List<RecommendationRuleDTO> rule;

}