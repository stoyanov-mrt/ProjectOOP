package exception;

public class InvalidTimeException extends NumberFormatException {
    public InvalidTimeException(String message) {
        super(message);
    }
}
