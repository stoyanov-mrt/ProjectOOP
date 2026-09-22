package cli.commands;

import exception.CalendarException;
import exception.InvalidCommandException;
import model.Calendar;
import service.CalendarManager;
import service.FileManager;

import java.io.File;

public class OpenCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public OpenCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            throw new InvalidCommandException("Usage: open <file>");
        }

        String fileName = args[1];
        Calendar calendar;
        try {
            calendar = fileManager.open(fileName);
        } catch (CalendarException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
            return;
        }

        calendarManager.openCalendar(calendar);
        System.out.println("Successfully opened " + new File(fileName).getName());
    }

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public boolean requiresOpenCalendar() {
        return false;
    }
}
