package service;

import exception.InvalidDateFormatException;
import model.Task;

import java.time.LocalDate;

public class TaskValidator {

    public void validateDate(Task task) {
        if (task.getDate() == null) {
            throw new InvalidDateFormatException("Date cannot be null");
        }
        if (task.getDate().isBefore(LocalDate.now())) {
            throw new InvalidDateFormatException("Date cannot be before current date");
        }
    }

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
