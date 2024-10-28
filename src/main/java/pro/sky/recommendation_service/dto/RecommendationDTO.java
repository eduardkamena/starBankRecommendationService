package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность рекомендации")
public class RecommendationDTO {

    @Schema(description = "Название рекомендации")
    private String product_name;

    @Schema(description = "Идентификатор рекомендации")
    private UUID product_id;

    @Schema(description = "Описание рекомендации")
    private String product_text;

}



