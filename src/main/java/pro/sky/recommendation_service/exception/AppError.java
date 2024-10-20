package pro.sky.recommendation_service.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Сущность ошибки")
public class AppError {

    @Schema(description = "Статус ошибки")
    private final int statusCode;

    @Schema(description = "Сообщение ошибки")
    private final String message;

}
