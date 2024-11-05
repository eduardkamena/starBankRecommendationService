package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Сущность рекомендации, связанная с клиентом")
public record UserRecommendationsDTO(@Schema(description = "Идентификатор клиента") UUID user_id,
                                     @Schema(description = "Рекомендации, доступные клиенту") List<ProductRecommendationsDTO> recommendations) {

}
