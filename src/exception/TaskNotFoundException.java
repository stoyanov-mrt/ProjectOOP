package exception;

public class TaskNotFoundException extends CalendarException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
