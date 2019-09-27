package ngkbtr.model.exception;

public class UserNotFoundException extends Exception {
    private static final String EXCEPTION_MESSAGE = "User not found";

    public UserNotFoundException() {
        super(EXCEPTION_MESSAGE);
    }
}
