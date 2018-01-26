package backendcodingchallenge.service.exceptions;

public class ExternalApiNotOkStatusException extends RuntimeException {
    public ExternalApiNotOkStatusException(String message) {
        super(message);
    }
}

