package ngkbtr.model.exception;

public class ForbiddenResourceException extends Exception{
    private static final String EXCEPTION_MESSAGE = "Forbidden resource";

    public ForbiddenResourceException() {
        super(EXCEPTION_MESSAGE);
    }
}
