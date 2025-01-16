package pro.sky.recommendation_service.exception;

/**
 * Исключение, выбрасываемое при отсутствии продукта в базе данных.
 * <p>
 * Используется для обработки ошибок, связанных с поиском продукта.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

}
