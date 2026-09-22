package cli.commands;

import exception.InvalidCommandException;
import parser.DateParser;
import service.CalendarManager;

import java.time.LocalDate;

public class HolidayCommand implements Command {
    private CalendarManager calendarManager;
    private DateParser dateParser;

    public HolidayCommand(CalendarManager calendarManager, DateParser dateParser) {
        this.calendarManager = calendarManager;
        this.dateParser = dateParser;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            throw new InvalidCommandException("Usage: holiday <date>");
        }

        LocalDate bookDate = dateParser.parseDate(args[1]);

        calendarManager.bookHoliday(bookDate);
    }
    @Override
    public String getName() {
        return "holiday";
    }
}
