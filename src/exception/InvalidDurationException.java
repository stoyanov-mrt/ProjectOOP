package exception;

/**
 * Thrown when a requested meeting duration is not usable, e.g. zero,
 * negative, or longer than a single working day.
 */
public class InvalidDurationException extends CalendarException {
    public InvalidDurationException(String message) {
        super(message);
    }
}
