package exception;

/**
 * Thrown when an operation on a task conflicts with a date already marked
 * as a holiday (e.g. booking a task on a holiday).
 */
public class HolidayException extends CalendarException {
    public HolidayException(String message) {
        super(message);
    }
}
