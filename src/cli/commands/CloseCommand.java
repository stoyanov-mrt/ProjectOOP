package cli.commands;

import exception.InvalidCommandException;
import service.CalendarManager;
import service.FileManager;

import java.io.File;

public class CloseCommand implements Command {
    private final CalendarManager calendarManager;
    private final FileManager fileManager;

    public CloseCommand(CalendarManager calendarManager, FileManager fileManager) {
        this.calendarManager = calendarManager;
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            throw new InvalidCommandException("Usage: close");
        }

        String fileName = fileManager.getCurrentFileName();
        fileManager.close();
        calendarManager.closeCalendar();
        System.out.println("Successfully closed " + new File(fileName).getName());
    }

    @Override
    public String getName() {
        return "close";
    }
}
