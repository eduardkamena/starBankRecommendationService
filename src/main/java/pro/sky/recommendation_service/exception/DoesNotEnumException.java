package pro.sky.recommendation_service.exception;

/**
 * Исключение, выбрасываемое при несоответствии значения перечислению.
 * <p>
 * Используется для обработки ошибок, связанных с неверными значениями перечислений.
 */
public class DoesNotEnumException extends RuntimeException {

    public DoesNotEnumException(String message) {
        super(message);
    }

}
