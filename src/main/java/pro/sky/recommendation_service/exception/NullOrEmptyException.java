package pro.sky.recommendation_service.exception;

/**
 * Исключение, выбрасываемое при передаче нулевых или пустых значений.
 * <p>
 * Используется для обработки ошибок, связанных с некорректными входными данными.
 */
public class NullOrEmptyException extends RuntimeException {

    public NullOrEmptyException(String message) {
        super(message);
    }

}
