package cli.commands;

import parser.DateParser;
import service.CalendarManager;

import java.time.LocalDate;

public class AgendaCommand implements Command {
    private DateParser dateParser;
    private CalendarManager calendarManager;

    public AgendaCommand(DateParser dateParser, CalendarManager calendarManager) {
        this.dateParser = dateParser;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }
        LocalDate agendaDate = dateParser.parseDate(args[1]);
        calendarManager.getAgenda(agendaDate);
    }
    @Override
    public String getName() {
        return "agenda";
    }
}
