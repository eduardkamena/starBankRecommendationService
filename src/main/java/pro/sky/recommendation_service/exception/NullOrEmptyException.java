package pro.sky.recommendation_service.exception;

public class NullOrEmptyException extends RuntimeException {

    public NullOrEmptyException(String message) {
        super(message);
    }

}
