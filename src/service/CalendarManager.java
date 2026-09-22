package service;

import exception.*;
import model.Calendar;
import model.Task;
import model.TimeSlot;
import parser.DateParser;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarManager {
    private Calendar currentCalendar;
    private boolean open = false;
    private TaskValidator taskValidator = new TaskValidator();
    private DateParser dateParser = new DateParser();

    public CalendarManager() {
        currentCalendar = new Calendar();
    }

    public void openCalendar(Calendar calendar) {
        currentCalendar = calendar;
        open = true;
    }

    public void closeCalendar() {
        currentCalendar = new Calendar();
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    public List<Task> getTasks() {
        return currentCalendar.getTasks();
    }

    public void bookTask(Task task) {
        taskValidator.validateTask(task);

        validateNotHoliday(task.getDate());

        validateNoOverlap(task);


        currentCalendar.addTask(task);
    }

    public void unbookTask(Task task) {
        if (!currentCalendar.removeTask(task)) {
            throw new TaskNotFoundException("The task doesn't exist");
        }
    }

    public Task findTaskByDateAndStartTime(LocalDate date, LocalTime startTime) {
        for (Task task : currentCalendar.getTasks()) {
            if (task.getDate().equals(date) && task.getStartTime().equals(startTime)) {
                return task;
            }
        }
        return null;
    }


    public void changeTask(LocalDate date, LocalTime startTime, String option, String newValue) {

        Task oldTask = findTaskByDateAndStartTime(date, startTime);

        if (oldTask == null) {
            throw new TaskNotFoundException("The task doesn't exist");
        }

        Task taskToChange = new Task(oldTask);
        option = option.toLowerCase();

        switch (option) {
            case "date":
                    LocalDate newDate = dateParser.parseDate(newValue);
                    taskToChange.setDate(newDate);
                break;
            case "starttime":
                        LocalTime newStartTime = dateParser.parseTime(newValue);
                        taskToChange.setStartTime(newStartTime);
                break;
            case "endtime":

                        LocalTime newEndTime = dateParser.parseTime(newValue);
                        taskToChange.setEndTime(newEndTime);
                break;
            case "name":
                    taskToChange.setName(newValue);
                break;
            case "note":
                    taskToChange.setNote(newValue);
                break;
            default:
                throw new InvalidCommandException("Unknown command: " + option);

        }
        unbookTask(oldTask);
        try {
            bookTask(taskToChange);
        } catch (CalendarException e) {
            bookTask(oldTask);
            throw e;
        }

    }

    public List<Task> getAgenda(LocalDate date) {
        List<Task> tasksByDate = currentCalendar.getTasksByDate(date);
        tasksByDate.sort(null);
        return tasksByDate;
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }

    public List<Task> findTaskByKeyword(String keyword) {
        List<Task> tasks = new ArrayList<>();

        keyword = keyword.toLowerCase();

        for (Task task : currentCalendar.getTasks()) {

            String name = task.getName() != null ? task.getName().toLowerCase() : "";

            String note = task.getNote() != null ? task.getNote().toLowerCase() : "";

            if (name.contains(keyword) || note.contains(keyword)) {
                tasks.add(task);
            }
        }

        return tasks;
    }


    protected void validateNoOverlap(Task taskToCheck) {
        for (Task task : currentCalendar.getTasks()) {
            if (task.getDate().equals(taskToCheck.getDate())) {
                if (taskToCheck.getStartTime().isBefore(task.getEndTime()) && taskToCheck.getEndTime().isAfter(task.getStartTime())) {
                    throw new TaskOverlapException("Overlap found");
                }
            }
        }

    }

    public void bookHoliday(LocalDate date) {
        validateHolidayDate(date);
        validateHolidayDoesNotExist(date);
        validateNoTasksOnDate(date);

        currentCalendar.addHoliday(date);
    }

    protected void validateNotHoliday(LocalDate date) {
        if (currentCalendar.getHolidays().contains(date)) {
            throw new HolidayException("Date is holiday");
        }
    }



    protected void validateHolidayDate(LocalDate date) {
        if (date == null) {
            throw new InvalidCommandException("Date is null");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new InvalidHolidayException("Cannot set dates before the current date");
        }
    }
    protected void validateHolidayDoesNotExist(LocalDate date) {
        if (currentCalendar.getHolidays().contains(date)) {
            throw new HolidayException("Date is already holiday");
        }
    }
    protected void validateNoTasksOnDate(LocalDate date) {
        if (!currentCalendar.getTasksByDate(date).isEmpty()) {
            throw new InvalidHolidayException("There are tasks on the date");
        }
    }


    protected double calculateTaskDuration(Task task) {
        Duration duration = Duration.between(task.getStartTime(), task.getEndTime());

        return duration.toMinutes() / 60.0;
    }

    protected double calculateDurationBetweenTasks(Task task1, Task task2) {
        Duration duration = Duration.between(task1.getEndTime(), task2.getStartTime());

        return duration.toMinutes() / 60.0;
    }

    public double calculateBusyHoursForDate(LocalDate date) {
        double busyHours = 0;
        List<Task> tasks = currentCalendar.getTasksByDate(date);
        for (Task task : tasks) {
            busyHours += calculateTaskDuration(task);
        }

        return busyHours;
    }

    public void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new InvalidDateFormatException("Invalid date range");
        }
        if (from.isAfter(to)) {
            throw new InvalidDateFormatException("From is after to");
        }
    }

    private boolean isDateInRange(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    public HashMap<DayOfWeek, Double> busyDays(LocalDate from, LocalDate to) {
        HashMap<DayOfWeek, Double> busyDays = new HashMap<>();

        for (Task task : currentCalendar.getTasks()) {
            if (isDateInRange(task.getDate(), from, to)) {
                DayOfWeek day = task.getDate().getDayOfWeek();
                double hours = calculateTaskDuration(task);
                if (busyDays.containsKey(day)) {
                    busyDays.put(day, busyDays.get(day) + hours);
                }
                else {
                    busyDays.put(day, hours);
                }
            }
        }

        return busyDays;
    }

    protected void validateWeekendDate(LocalDate date) {
        validateDate(date);

        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new InvalidDateException("Invalid date is on the weekend");
        }
    }

    public void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidDateException("Date is null");
        }
    }

    protected List<Task> getSortedTasksByStartTime(LocalDate date) {
        List<Task> tasks = currentCalendar.getTasksByDate(date);
        tasks.sort(null);

        return tasks;
    }

    protected TimeSlot findFreeSlot(List<Task> tasks, double hours, LocalDate date) {
        int i = 0;
        LocalTime workStart = dateParser.parseTime("8:00");
        LocalTime workEnd = dateParser.parseTime("17:00");

        if (hours <= 0 || hours > 9) {
            throw new InvalidDurationException("Hours out of range");
        }

        long minutes = Math.round(hours * 60);
        LocalTime endTimeTask = workStart.plusMinutes(minutes);

        if (tasks.isEmpty()) {

            if (!endTimeTask.isAfter(workEnd)) {
                return new TimeSlot (date, workStart, endTimeTask);
            }

            return null;
        }

        Task firstTask = tasks.getFirst();
        double freeHoursBeforeFirstTask = Duration.between(workStart, firstTask.getStartTime()).toMinutes() / 60.0;
        Task lastTask = tasks.getLast();
        double freeHoursAfterLastTask = Duration.between(lastTask.getEndTime(), workEnd).toMinutes() / 60.0;

        if (freeHoursBeforeFirstTask >= hours) {
            return new TimeSlot(date, workStart, endTimeTask);
        }


        while (i < tasks.size() - 1) {

            if (calculateDurationBetweenTasks(tasks.get(i), tasks.get(i + 1)) >= hours) {
                return new TimeSlot(date, tasks.get(i).getEndTime(), tasks.get(i).getEndTime().plusMinutes(minutes));
            }

            i++;
        }

        if (freeHoursAfterLastTask >= hours) {
            return new TimeSlot(date, lastTask.getEndTime(), lastTask.getEndTime().plusMinutes(minutes));
        }

        return null;
    }

    public TimeSlot findSlot(LocalDate fromDate, double hours) {
        LocalDate date = fromDate;
        while (date.isBefore(fromDate.plusDays(365))) {
            try {
                validateNotHoliday(date);
            } catch (HolidayException e) {
                date = date.plusDays(1);
                continue;
            }
            try {
                validateWeekendDate(date);
            } catch (DateException e) {
                date = date.plusDays(1);
                continue;
            }

            List<Task> sortedTasksForTheDate = getSortedTasksByStartTime(date);

            TimeSlot freeSlot = findFreeSlot(sortedTasksForTheDate, hours, date);

            if (freeSlot != null) {
                return freeSlot;
            }


            date = date.plusDays(1);
        }

        return null;
    }

    /**
     * Like {@link #findSlot}, but the returned slot must also be free in
     * every calendar in {@code otherCalendars}.
     */
    public TimeSlot findSlotWith(LocalDate fromDate, double hours, List<Calendar> otherCalendars) {
        LocalDate date = fromDate;
        while (date.isBefore(fromDate.plusDays(365))) {
            if (isHolidayInAny(date, otherCalendars)) {
                date = date.plusDays(1);
                continue;
            }
            try {
                validateWeekendDate(date);
            } catch (DateException e) {
                date = date.plusDays(1);
                continue;
            }

            List<Task> combinedTasks = new ArrayList<>(currentCalendar.getTasksByDate(date));
            for (Calendar other : otherCalendars) {
                combinedTasks.addAll(other.getTasksByDate(date));
            }
            combinedTasks.sort(null);

            TimeSlot freeSlot = findFreeSlot(combinedTasks, hours, date);

            if (freeSlot != null) {
                return freeSlot;
            }

            date = date.plusDays(1);
        }

        return null;
    }

    private boolean isHolidayInAny(LocalDate date, List<Calendar> otherCalendars) {
        if (currentCalendar.getHolidays().contains(date)) {
            return true;
        }
        for (Calendar other : otherCalendars) {
            if (other.getHolidays().contains(date)) {
                return true;
            }
        }
        return false;
    }
}
