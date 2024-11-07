package pro.sky.recommendation_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.sky.recommendation_service.enums.RulesQueryENUM;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность объекта запроса для правила рекомендации")
public class RulesDTO {

    @JsonIgnore
    @Schema(description = "Идентификатор объекта запроса для правила рекомендации в БД")
    private UUID id;

    @Schema(description = "Название объекта запроса для правила рекомендации")
    private RulesQueryENUM query;

    @Schema(description = "Аргументы объекта запроса для правила рекомендации")
    private List<String> arguments;

    @Schema(description = "Соответствие объекта запроса для правила рекомендации)")
    private boolean negate;

}
