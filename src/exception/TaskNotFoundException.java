package exception;

/**
 * Thrown when a task looked up by date and start time (e.g. for
 * {@code unbook} or {@code change}) does not exist.
 */
public class TaskNotFoundException extends CalendarException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
