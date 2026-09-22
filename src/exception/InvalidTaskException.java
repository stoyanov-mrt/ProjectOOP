package exception;

/**
 * Thrown when a {@link model.Task} fails validation and cannot be booked.
 */
public class InvalidTaskException extends CalendarException {
    public InvalidTaskException(String message) {
        super(message);
    }
}
