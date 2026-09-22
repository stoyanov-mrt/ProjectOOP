package cli.commands;

import model.Task;
import service.CalendarManager;

import java.util.List;

public class FindCommand implements Command {
    private CalendarManager calendarManager;

    public FindCommand(CalendarManager calendarManager) {
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        String keyword = args[1];

        List<Task> tasks = calendarManager.findTaskByKeyword(keyword);

        for (Task task : tasks) {
            System.out.println(task);
        }
    }
    @Override
    public String getName() {
        return "find";
    }
}
