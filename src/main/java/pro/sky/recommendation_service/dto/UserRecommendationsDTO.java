package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Сущность клиента")
public class UserRecommendationsDTO {

    @Schema(description = "Идентификатор клиента")
    private final UUID user_id;

    @Schema(description = "Рекомендации, доступные клиенту")
    private final List<ProductRecommendationsDTO> recommendations;

}
