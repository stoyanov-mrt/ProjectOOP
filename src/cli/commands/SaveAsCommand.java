package cli.commands;

import exception.InvalidCommandException;
import service.CalendarManager;
import service.FileManager;

import java.io.File;

public class SaveAsCommand implements Command {
    private final CalendarManager calendarManager;
    private final FileManager fileManager;

    public SaveAsCommand(CalendarManager calendarManager, FileManager fileManager) {
        this.calendarManager = calendarManager;
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            throw new InvalidCommandException("Usage: saveas <file>");
        }

        String fileName = args[1];
        fileManager.saveAs(calendarManager.getCurrentCalendar(), fileName);
        System.out.println("Successfully saved " + new File(fileName).getName());
    }

    @Override
    public String getName() {
        return "saveas";
    }
}
