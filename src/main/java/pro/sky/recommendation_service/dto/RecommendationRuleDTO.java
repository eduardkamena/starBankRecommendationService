package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import pro.sky.recommendation_service.entity.enums.Queries;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Сущность правила")
public class RecommendationRuleDTO {

    @Schema(description = "Описание запроса")
    private Queries query;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Аргументы запроса")
    private List<String> arguments;

    @Schema(description = "Отрицание запроса")
    private boolean negate;

}
