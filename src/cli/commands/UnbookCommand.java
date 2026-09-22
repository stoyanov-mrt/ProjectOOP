package cli.commands;

import model.Task;
import parser.DateParser;
import service.CalendarManager;

import java.time.LocalDate;
import java.time.LocalTime;

public class UnbookCommand implements Command {
    private DateParser dateParser;
    private CalendarManager calendarManager;

    public UnbookCommand(DateParser dateParser, CalendarManager calendarManager) {
        this.dateParser = dateParser;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        LocalDate unbookDate = dateParser.parseDate(args[1]);
        LocalTime unbookStartTime = dateParser.parseTime(args[2]);
        LocalTime unbookEndTime = dateParser.parseTime(args[3]);

        Task taskToUnbook = new Task( unbookDate, unbookStartTime, unbookEndTime);
        calendarManager.unbookTask(taskToUnbook);
    }
    @Override
    public String getName() {
        return "unbook";
    }
}
