package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Calendar {
    private List<Task> tasks;
    private Set<LocalDate> holidays;

    public Calendar() {
        tasks = new ArrayList<Task>();
        holidays = new HashSet<LocalDate>();
    }
    public Calendar(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
    public boolean removeTask(Task task) {
        return tasks.remove(task);
    }
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
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }
    public void addHoliday(LocalDate date) {
        holidays.add(date);
    }
    public Set<LocalDate> getHolidays() {
        return new HashSet<>(holidays);
    }

}
