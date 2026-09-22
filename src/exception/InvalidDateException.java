package exception;

/**
 * Thrown when a date is structurally valid but not usable in context, e.g.
 * a {@code null} date or a weekend date where only business days are
 * allowed.
 */
public class InvalidDateException extends DateException {
    public InvalidDateException(String message) {
        super(message);
    }
}
