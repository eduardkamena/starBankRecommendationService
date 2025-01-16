package pro.sky.recommendation_service.exception;

/**
 * Класс, представляющий ошибку приложения.
 * <p>
 * Используется для передачи информации об ошибке в ответах API.
 */
public record AppError(int statusCode, String message) {

}
