package cli.commands;

import exception.InvalidCommandException;
import service.CalendarManager;
import service.FileManager;

import java.io.File;

public class SaveCommand implements Command {
    private final CalendarManager calendarManager;
    private final FileManager fileManager;

    public SaveCommand(CalendarManager calendarManager, FileManager fileManager) {
        this.calendarManager = calendarManager;
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            throw new InvalidCommandException("Usage: save");
        }

        fileManager.save(calendarManager.getCurrentCalendar());
        System.out.println("Successfully saved " + new File(fileManager.getCurrentFileName()).getName());
    }

    @Override
    public String getName() {
        return "save";
    }
}
