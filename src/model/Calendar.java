package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The in-memory data of a personal calendar: a set of booked
 * {@link Task}s and a set of dates marked as holidays. Holds no business
 * rules of its own (validation, overlap/holiday checks) — those live in
 * {@link service.CalendarManager}, which owns the currently open
 * {@code Calendar} instance.
 */
public class Calendar {
    private List<Task> tasks;
    private Set<LocalDate> holidays;

    public Calendar() {
        tasks = new ArrayList<Task>();
        holidays = new HashSet<LocalDate>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task matching {@code task} by {@link Task#equals}
     * (date + start time + end time).
     *
     * @return {@code true} if a matching task was found and removed
     */
    public boolean removeTask(Task task) {
        return tasks.remove(task);
    }

    /**
     * @return a new list of every task on {@code date}, in no particular order
     */
    public List<Task> getTasksByDate(LocalDate date) {
        List<Task> result = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getDate().equals(date)) {
                result.add(task);
            }
        }

        return result;
    }

    public void printTasks() {
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    /**
     * @return a defensive copy of all booked tasks
     */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public void addHoliday(LocalDate date) {
        holidays.add(date);
    }

    /**
     * @return a defensive copy of the set of holiday dates
     */
    public Set<LocalDate> getHolidays() {
        return new HashSet<>(holidays);
    }

}
