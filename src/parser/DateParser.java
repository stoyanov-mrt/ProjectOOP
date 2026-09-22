package parser;

import exception.InvalidDateFormatException;
import exception.InvalidTimeFormatException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Parses the date and time strings the user types on the command line into
 * {@link LocalDate}/{@link LocalTime}, translating format errors into the
 * project's own {@link exception.CalendarException} subtypes so they are
 * reported as a clean {@code "Error: ..."} message instead of crashing.
 */
public class DateParser {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
    List<DateTimeFormatter> dateFormatters = Arrays.asList(formatter, formatter2);

    /**
     * Parses {@code date} as either {@code dd-MM-yyyy} or {@code yyyy-MM-dd}.
     *
     * @throws InvalidDateFormatException if {@code date} matches neither format
     */
    public LocalDate parseDate(String date) {
        for (DateTimeFormatter formatter : dateFormatters) {
            try {
               return LocalDate.parse(date, formatter);
            }
            catch (DateTimeParseException e) {
                continue;
            }
        }
        throw new InvalidDateFormatException("Wrong date format");
    }

    /**
     * Parses {@code time} as {@code H:mm} (e.g. {@code "9:00"} or {@code "17:30"}).
     *
     * @throws InvalidTimeFormatException if {@code time} does not match that format
     */
    public LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time, timeFormatter);
        } catch (DateTimeParseException e) {
            throw new InvalidTimeFormatException("Wrong time format");
        }
    }

}
