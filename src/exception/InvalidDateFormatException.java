package exception;

/**
 * Thrown when a date or time string cannot be parsed, or when a task's
 * fields fail validation (e.g. missing name, end time before start time).
 */
public class InvalidDateFormatException extends DateException {
    public InvalidDateFormatException(String message) {
        super(message);
    }
}
