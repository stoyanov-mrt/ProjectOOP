package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A single calendar appointment: a name and note on a given date, between
 * a start and end time. Two tasks are considered equal (and therefore
 * conflicting) when they share the same date, start time, and end time.
 * Natural ordering is chronological: by date, then by start time.
 */
public class Task implements Comparable<Task>{
    private String name;
    private String note;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public Task(String name, String note, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.note = note;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Creates a task with no name/note, identified only by date and time
     * range. Used to look up or remove an existing task (see
     * {@link service.CalendarManager#unbookTask}), where the name and note
     * are irrelevant to {@link #equals}.
     */
    public Task(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Copy constructor. Used when a task from another calendar needs to be
     * inserted into this one (see {@link cli.commands.MergeCommand}) or
     * mutated speculatively without touching the original (see
     * {@link service.CalendarManager#changeTask}).
     */
    public Task(Task task) {
        this.name = task.name;
        this.note = task.note;
        this.date = task.date;
        this.startTime = task.startTime;
        this.endTime = task.endTime;
    }

    public String getName() {
        return name;
    }
    public String getNote() {
        return note;
    }
    public LocalDate getDate() {
        return date;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Note: " + note + ", Date: " + date + ", Start: " + startTime + ", End: " + endTime;
    }

    /**
     * Orders tasks chronologically: earlier dates first, then earlier
     * start times first on the same date.
     */
    @Override
    public int compareTo(Task o) {
        int dateComparison = this.date.compareTo(o.date);
        if (dateComparison != 0) {
            return dateComparison;
        }
        return this.startTime.compareTo(o.startTime);
    }

    /**
     * Two tasks are equal when they occupy the same date and time range,
     * regardless of name or note. This is what lets {@code unbook} identify
     * a task to remove using only date/start/end.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Task anotherTask = (Task) obj;

        if (this.date.equals(anotherTask.getDate()) && this.getStartTime().equals(anotherTask.getStartTime()) && this.endTime.equals(anotherTask.getEndTime())) {
            return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime);
    }
}
