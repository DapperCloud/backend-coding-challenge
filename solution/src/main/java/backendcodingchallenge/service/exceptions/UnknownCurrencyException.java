package backendcodingchallenge.service.exceptions;

public class UnknownCurrencyException extends RuntimeException {
    public UnknownCurrencyException(String message) {
        super(message);
    }
}
