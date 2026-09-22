package cli.commands;

import exception.HolidayException;
import exception.InvalidCommandException;
import exception.TaskOverlapException;
import model.Calendar;
import model.Task;
import parser.DateParser;
import service.CalendarManager;
import service.FileManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * {@code merge <calendar> [<calendar> ...]} transfers every task from the
 * given calendar file(s) into the current calendar (bonus: supports more
 * than one calendar file). When a transferred task conflicts with an
 * existing one, the user is asked interactively whether to skip it or move
 * it to a different date/time.
 */
public class MergeCommand implements Command {
    private final CalendarManager calendarManager;
    private final FileManager fileManager;
    private final DateParser dateParser;
    private final Scanner scanner;

    public MergeCommand(CalendarManager calendarManager, FileManager fileManager, DateParser dateParser, Scanner scanner) {
        this.calendarManager = calendarManager;
        this.fileManager = fileManager;
        this.dateParser = dateParser;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            throw new InvalidCommandException("Usage: merge <calendar> [<calendar> ...]");
        }

        for (int i = 1; i < args.length; i++) {
            mergeFrom(args[i]);
        }
    }

    private void mergeFrom(String fileName) {
        Calendar other = fileManager.peek(fileName);

        for (LocalDate holiday : other.getHolidays()) {
            calendarManager.getCurrentCalendar().addHoliday(holiday);
        }

        for (Task task : other.getTasks()) {
            mergeTask(task, fileName);
        }

        System.out.println("Successfully merged " + fileName);
    }

    private void mergeTask(Task task, String sourceFile) {
        while (true) {
            try {
                calendarManager.bookTask(new Task(task));
                return;
            } catch (TaskOverlapException e) {
                task = resolveOverlap(task, sourceFile);
                if (task == null) {
                    return;
                }
            } catch (HolidayException e) {
                System.out.println("Skipping [" + sourceFile + "] task \"" + task.getName()
                        + "\" on " + task.getDate() + ": that date is a holiday.");
                return;
            }
        }
    }

    /**
     * @return a task moved to a new date/time to retry booking, or
     *         {@code null} if the user chose to skip it.
     */
    private Task resolveOverlap(Task task, String sourceFile) {
        System.out.println("Conflict merging [" + sourceFile + "] task \"" + task.getName()
                + "\" (" + task.getDate() + " " + task.getStartTime() + "-" + task.getEndTime()
                + "): it overlaps an existing task.");
        System.out.print("Keep the existing task and skip this one, or move this one to a new date/time? (skip/move): ");

        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.startsWith("m")) {
            System.out.print("New date (dd-MM-yyyy): ");
            LocalDate newDate = dateParser.parseDate(scanner.nextLine().trim());
            System.out.print("New start time (H:mm): ");
            LocalTime newStart = dateParser.parseTime(scanner.nextLine().trim());
            System.out.print("New end time (H:mm): ");
            LocalTime newEnd = dateParser.parseTime(scanner.nextLine().trim());

            return new Task(task.getName(), task.getNote(), newDate, newStart, newEnd);
        }

        System.out.println("Skipped \"" + task.getName() + "\".");
        return null;
    }

    @Override
    public String getName() {
        return "merge";
    }
}
