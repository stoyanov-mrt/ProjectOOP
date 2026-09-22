package cli.commands;

import exception.InvalidCommandException;
import model.Task;
import parser.DateParser;
import service.CalendarManager;

import java.time.LocalDate;
import java.util.List;

/** {@code agenda <date>} — lists every task on {@code date}, in chronological order. */
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
            throw new InvalidCommandException("Usage: agenda <date>");
        }
        LocalDate agendaDate = dateParser.parseDate(args[1]);
        List<Task> tasks = calendarManager.getAgenda(agendaDate);

        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for " + agendaDate + ".");
            return;
        }

        for (Task task : tasks) {
            System.out.println(task);
        }
    }
    @Override
    public String getName() {
        return "agenda";
    }
}
