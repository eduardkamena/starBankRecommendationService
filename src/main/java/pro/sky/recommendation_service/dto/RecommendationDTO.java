package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pro.sky.recommendation_service.enitites.Rule;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Schema(description = "Сущность рекомендации")
public class RecommendationDTO {

    @Schema(description = "Название рекомендации")
    private final String name;

    @Schema(description = "Идентификатор рекомендации")
    private final UUID id;

    @Schema(description = "Описание рекомендации")
    private final String text;

    @Schema(description = "Список правил")
    private List<Rule> rules;

}
