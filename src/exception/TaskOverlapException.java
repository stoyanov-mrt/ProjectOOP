package exception;

/**
 * Thrown when booking a task would overlap another task already on the
 * same date.
 */
public class TaskOverlapException extends CalendarException {
    public TaskOverlapException(String message) {
        super(message);
    }
}
