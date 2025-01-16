package pro.sky.recommendation_service.exception;

/**
 * Исключение, выбрасываемое при отсутствии правила в базе данных.
 * <p>
 * Используется для обработки ошибок, связанных с поиском правила.
 */
public class RuleNotFoundException extends RuntimeException {

    public RuleNotFoundException(String message) {
        super(message);
    }

}
