package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Сущность объекта запроса")
public class RulesDTO {

    @Schema(description = "Название объекта запроса")
    private String query;

    @Schema(description = "Аргументы объекта запроса")
    private String[] arguments;

    @Schema(description = "Подтверждение объекта запроса")
    private boolean negate;

}
