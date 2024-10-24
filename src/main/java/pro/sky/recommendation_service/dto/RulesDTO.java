package pro.sky.recommendation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Сущность объекта запроса")
public class RulesDTO {

    private UUID id;
    private String query;
    private String[] arguments;
    private boolean negate;

}
