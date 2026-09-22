package exception;

/**
 * Base type for exceptions about an invalid or unusable {@link java.time.LocalTime}.
 */
public class InvalidTimeException extends CalendarException {
    public InvalidTimeException(String message) {
        super(message);
    }
}
