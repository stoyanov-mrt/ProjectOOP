package exception;

/**
 * Base type for every unchecked exception raised by this application.
 * Caught by {@link cli.Main}'s command loop and reported to the user as
 * {@code "Error: <message>"} instead of crashing the program.
 */
public class CalendarException extends RuntimeException {
    public CalendarException(String message) {
        super(message);
    }
}
