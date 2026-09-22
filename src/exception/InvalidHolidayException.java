package exception;

/**
 * Thrown when a date cannot be booked as a holiday, e.g. it is in the past
 * or already has tasks scheduled on it.
 */
public class InvalidHolidayException extends CalendarException {
    public InvalidHolidayException(String message) {
        super(message);
    }
}
