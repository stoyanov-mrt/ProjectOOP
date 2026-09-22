package exception;

/**
 * Base type for exceptions about an invalid or unusable {@link java.time.LocalDate}.
 */
public class DateException extends CalendarException {
    public DateException(String message) {
        super(message);
    }
}
