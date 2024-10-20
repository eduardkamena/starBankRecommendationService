package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Сущность рекомендации")
public class RecommendationDTO {

    @Schema(description = "Название рекомендации")
    private final String name;

    @Schema(description = "Идентификатор рекомендации")
    private final UUID id;

    @Schema(description = "Описание рекомендации")
    private final String text;

}
