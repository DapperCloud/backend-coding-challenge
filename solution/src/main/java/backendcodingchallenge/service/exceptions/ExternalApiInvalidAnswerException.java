package backendcodingchallenge.service.exceptions;

public class ExternalApiInvalidAnswerException extends RuntimeException {
    public ExternalApiInvalidAnswerException(String message) {
        super(message);
    }
}

