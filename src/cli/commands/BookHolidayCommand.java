package cli.commands;

import parser.DateParser;
import service.CalendarManager;

import java.time.LocalDate;

public class BookHolidayCommand implements Command {
    private CalendarManager calendarManager;
    private DateParser dateParser;

    public BookHolidayCommand(CalendarManager calendarManager, DateParser dateParser) {
        this.calendarManager = calendarManager;
        this.dateParser = dateParser;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        LocalDate bookDate = dateParser.parseDate(args[1]);

        calendarManager.bookHoliday(bookDate);
    }
    @Override
    public String getName() {
        return "bookholiday";
    }
}
