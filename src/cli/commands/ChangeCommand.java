package cli.commands;

import exception.TaskNotFoundException;
import model.Task;
import parser.DateParser;
import service.CalendarManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class ChangeCommand implements Command {
    private DateParser dateParser;
    private CalendarManager calendarManager;

    public ChangeCommand(DateParser dateParser, CalendarManager calendarManager) {
        this.dateParser = dateParser;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {

        if (args.length != 5) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        LocalDate date = dateParser.parseDate(args[1]);
        LocalTime startTime = dateParser.parseTime(args[2]);
        String option = args[3];
        String newValue = args[4];

        calendarManager.changeTask(date, startTime, option, newValue);

    }
    @Override
    public String getName() {
        return "change";
    }
}
