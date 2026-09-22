package cli.commands;

import exception.InvalidCommandException;
import exception.InvalidTimeFormatException;
import model.Calendar;
import model.TimeSlot;
import parser.DateParser;
import service.CalendarManager;
import service.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code findslotwith <fromdate> <hours> <calendar> [<calendar> ...]} finds
 * a free slot that is available both in the current calendar and in every
 * referenced calendar file (bonus: supports more than one calendar file).
 */
public class FindSlotWithCommand implements Command {
    private final CalendarManager calendarManager;
    private final DateParser dateParser;
    private final FileManager fileManager;

    public FindSlotWithCommand(CalendarManager calendarManager, DateParser dateParser, FileManager fileManager) {
        this.calendarManager = calendarManager;
        this.dateParser = dateParser;
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 4) {
            throw new InvalidCommandException("Usage: findslotwith <fromdate> <hours> <calendar> [<calendar> ...]");
        }

        LocalDate fromDate = dateParser.parseDate(args[1]);

        double hours;
        try {
            hours = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            throw new InvalidTimeFormatException("Invalid duration format");
        }

        List<Calendar> otherCalendars = new ArrayList<>();
        for (int i = 3; i < args.length; i++) {
            otherCalendars.add(fileManager.peek(args[i]));
        }

        TimeSlot timeSlot = calendarManager.findSlotWith(fromDate, hours, otherCalendars);

        if (timeSlot == null) {
            System.out.println("No available slot found.");
            return;
        }

        System.out.println(timeSlot);
    }

    @Override
    public String getName() {
        return "findslotwith";
    }
}
