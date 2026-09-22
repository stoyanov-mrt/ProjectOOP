package exception;

/**
 * Thrown when the user enters an unknown command, the wrong number of
 * arguments, or an unrecognized option value.
 */
public class InvalidCommandException extends CalendarException {
    public InvalidCommandException(String message) {
        super(message);
    }
}
