package service;

import exception.InvalidDateFormatException;
import model.Task;

import java.time.LocalDate;

/**
 * Field-level validation for a {@link Task}, independent of the rest of
 * the calendar (no overlap or holiday checks — see
 * {@link CalendarManager#bookTask} for those).
 */
public class TaskValidator {

    /** @throws InvalidDateFormatException if the task's date is {@code null} or before today */
    public void validateDate(Task task) {
        if (task.getDate() == null) {
            throw new InvalidDateFormatException("Date cannot be null");
        }
        if (task.getDate().isBefore(LocalDate.now())) {
            throw new InvalidDateFormatException("Date cannot be before current date");
        }
    }

    /** @throws InvalidDateFormatException if start/end time is {@code null} or start is not before end */
    public void validateTimeRange(Task task) {
        if (task.getStartTime() == null) {
            throw new InvalidDateFormatException("Task start time is null");
        }
        if (task.getEndTime() == null) {
            throw new InvalidDateFormatException("Task end time is null");
        }
        if (task.getStartTime().equals(task.getEndTime()) || task.getStartTime().isAfter(task.getEndTime())) {
            throw new InvalidDateFormatException("Invalid time range");
        }
    }

    /**
     * Runs every field-level check: date, time range, and a non-empty name.
     *
     * @throws InvalidDateFormatException if any check fails
     */
    public void validateTask(Task task) {
        validateDate(task);
        validateTimeRange(task);
        if (task.getName() == null) {
            throw new InvalidDateFormatException("Task name is required");
        }
        if (task.getName().isEmpty()) {
            throw new InvalidDateFormatException("Task name cannot be empty");
        }
    }

}
