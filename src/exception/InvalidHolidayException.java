package exception;

public class InvalidHolidayException extends RuntimeException {
    public InvalidHolidayException(String message) {
        super(message);
    }
}
