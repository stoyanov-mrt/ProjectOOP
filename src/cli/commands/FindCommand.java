package cli.commands;

import exception.InvalidCommandException;
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
            throw new InvalidCommandException("Usage: find <string>");
        }

        String keyword = args[1];

        List<Task> tasks = calendarManager.findTaskByKeyword(keyword);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found matching \"" + keyword + "\".");
            return;
        }

        for (Task task : tasks) {
            System.out.println(task);
        }
    }
    @Override
    public String getName() {
        return "find";
    }
}
