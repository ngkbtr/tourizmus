package ngkbtr.model.exception;

public class FailedAuthenticationException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Authentication failed";

    public FailedAuthenticationException() {
        super(EXCEPTION_MESSAGE);
    }

    public FailedAuthenticationException(String message) {
        super(message);
    }
}
