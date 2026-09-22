package exception;

/**
 * Thrown when a calendar file cannot be read, parsed, or written by
 * {@link service.FileManager} or {@link persistence.CalendarSerializer}.
 */
public class FileOperationException extends CalendarException {
    public FileOperationException(String message) {
        super(message);
    }
}
