package pro.sky.recommendation_service.exception;

/**
 * Исключение, выбрасываемое при отсутствии пользователя в базе данных.
 * <p>
 * Используется для обработки ошибок, связанных с поиском пользователя.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

}
