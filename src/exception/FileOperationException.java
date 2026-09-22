package exception;

import java.io.FileNotFoundException;

public class FileOperationException extends CalendarException {
    public FileOperationException(String message) {
        super(message);
    }
}
