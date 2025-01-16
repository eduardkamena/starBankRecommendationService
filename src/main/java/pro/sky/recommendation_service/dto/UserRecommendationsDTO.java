package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

/**
 * Класс, представляющий DTO (Data Transfer Object) для рекомендаций, связанных с пользователем.
 * <p>
 * Используется для передачи данных о рекомендациях, доступных пользователю, между слоями приложения.
 */
@Schema(description = "Сущность рекомендации, связанная с клиентом")
public record UserRecommendationsDTO(@Schema(description = "Идентификатор клиента") UUID userId,
                                     @Schema(description = "Рекомендации, доступные клиенту") List<ProductRecommendationsDTO> recommendations) {

}
