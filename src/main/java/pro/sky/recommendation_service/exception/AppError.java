package pro.sky.recommendation_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppError {

    private final int statusCode;

    private final String message;

}
