package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A free window of time found by {@link service.CalendarManager#findSlot}
 * or {@link service.CalendarManager#findSlotWith}: a candidate date and
 * start/end time for a new meeting.
 */
public class TimeSlot {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
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

    @Override
    public String toString() {
        return "Date: " + date + " Start: " + startTime + " End: " + endTime;
    }
}
