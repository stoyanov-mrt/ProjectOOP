package cli.commands;


import exception.InvalidCommandException;
import parser.DateParser;
import service.CalendarManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BusydaysCommand implements Command {
    private CalendarManager calendarManager;
    private DateParser dateParser;

    public BusydaysCommand(CalendarManager calendarManager, DateParser dateParser) {
        this.calendarManager = calendarManager;
        this.dateParser = dateParser;
    }


    @Override
    public void execute(String[] args) {
        if (args.length != 3) {
            throw new InvalidCommandException("Usage: busydays <from> <to>");
        }

        LocalDate fromDate = dateParser.parseDate(args[1]);
        LocalDate toDate = dateParser.parseDate(args[2]);

        calendarManager.validateDateRange(fromDate, toDate);

        Map<DayOfWeek, Double> busyDays = calendarManager.busyDays(fromDate, toDate);

        List<Map.Entry<DayOfWeek, Double>> entries = new ArrayList<>(busyDays.entrySet());

        entries.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<DayOfWeek, Double> entry : entries) {
            System.out.printf("%s -> %.2f hours%n", entry.getKey(), entry.getValue());
        }

    }


    @Override
    public String getName() {
        return "busydays";
    }
}
