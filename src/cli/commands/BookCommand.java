package cli.commands;

import model.Task;
import parser.DateParser;
import service.CalendarManager;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookCommand implements Command {
    private DateParser dateParser;
    private CalendarManager calendarManager;

    public BookCommand(DateParser dateParser, CalendarManager calendarManager) {
        this.dateParser = dateParser;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 6) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        String name = args[4];
        String note = args[5];
        LocalDate date = dateParser.parseDate(args[1]);
        LocalTime startTime = dateParser.parseTime(args[2]);
        LocalTime endTime = dateParser.parseTime(args[3]);
        Task taskToBook = new Task(name, note, date, startTime, endTime);
        calendarManager.bookTask(taskToBook);
    }
    @Override
    public String getName() {
        return "book";
    }
}
