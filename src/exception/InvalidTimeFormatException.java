package exception;

/**
 * Thrown when a time or duration string cannot be parsed (e.g. an
 * {@code <hours>} argument that isn't a number, or a time not matching
 * {@code H:mm}).
 */
public class InvalidTimeFormatException extends InvalidTimeException {
    public InvalidTimeFormatException(String message) {
        super(message);
    }
}
