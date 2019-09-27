package ngkbtr.model.exception;

public class TokenExpiredException extends Exception {
    private static final String EXCEPTION_MESSAGE = "Token has been expired";

    public TokenExpiredException() {
        super(EXCEPTION_MESSAGE);
    }
}
