package cli.commands;

import exception.InvalidTimeFormatException;
import model.TimeSlot;
import parser.DateParser;
import service.CalendarManager;

import java.time.LocalDate;

public class FindSlotCommand implements Command {
    private CalendarManager calendarManager;
    private DateParser dateParser;

    public FindSlotCommand(CalendarManager calendarManager, DateParser dateParser) {
        this.calendarManager = calendarManager;
        this.dateParser = dateParser;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        double hours;
        LocalDate fromDate = dateParser.parseDate(args[1]);
        try {
            hours = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            throw new InvalidTimeFormatException("Invalid duration format");
        }

        TimeSlot timeSlot = calendarManager.findSlot(fromDate, hours);

        if (timeSlot == null) {
            System.out.println("No available slot found.");
            return;
        }

        System.out.println(timeSlot);


    }

    @Override
    public String getName() {
        return "findslot";
    }
}
