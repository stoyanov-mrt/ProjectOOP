package parser;

import exception.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateParser {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
    List<DateTimeFormatter> dateFormatters = Arrays.asList(formatter, formatter2);


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
    public LocalTime parseTime(String time) {
        return LocalTime.parse(time, timeFormatter);
    }

}
