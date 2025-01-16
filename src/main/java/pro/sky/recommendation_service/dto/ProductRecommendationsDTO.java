package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Класс, представляющий DTO (Data Transfer Object) для продукта рекомендации.
 * <p>
 * Используется для передачи данных о продукте рекомендации между слоями приложения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность продукта рекомендации")
public class ProductRecommendationsDTO {

    @Schema(description = "Название продукта рекомендации")
    private String productName;

    @Schema(description = "Идентификатор продукта рекомендации")
    private UUID productId;

    @Schema(description = "Описание продукта рекомендации")
    private String productText;

}
