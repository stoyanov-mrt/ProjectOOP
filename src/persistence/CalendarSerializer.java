package persistence;

import exception.FileOperationException;
import model.Calendar;
import model.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Converts a {@link Calendar} to and from the plain-text file format used
 * for persistence: a {@code #HOLIDAYS} section listing one ISO date per
 * line, followed by a {@code #TASKS} section listing one pipe-separated
 * task per line ({@code date|startTime|endTime|name|note}).
 */
public class CalendarSerializer {

    private static final String HOLIDAYS_HEADER = "#HOLIDAYS";
    private static final String TASKS_HEADER = "#TASKS";
    private static final String FIELD_SEPARATOR = "|";
    private static final String FIELD_SEPARATOR_REGEX = "(?<!\\\\)\\|";

    public String serialize(Calendar calendar) {
        StringBuilder content = new StringBuilder();

        content.append(HOLIDAYS_HEADER).append(System.lineSeparator());
        for (LocalDate holiday : calendar.getHolidays()) {
            content.append(holiday).append(System.lineSeparator());
        }

        content.append(TASKS_HEADER).append(System.lineSeparator());
        for (Task task : calendar.getTasks()) {
            content.append(task.getDate()).append(FIELD_SEPARATOR)
                    .append(task.getStartTime()).append(FIELD_SEPARATOR)
                    .append(task.getEndTime()).append(FIELD_SEPARATOR)
                    .append(escape(task.getName())).append(FIELD_SEPARATOR)
                    .append(escape(task.getNote()))
                    .append(System.lineSeparator());
        }

        return content.toString();
    }

    public Calendar deserialize(String content) {
        Calendar calendar = new Calendar();
        String[] lines = content.split("\\r?\\n");
        String section = null;

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            if (line.equals(HOLIDAYS_HEADER) || line.equals(TASKS_HEADER)) {
                section = line;
                continue;
            }
            if (section == null) {
                throw new FileOperationException("Invalid calendar file: expected a section header, found: " + line);
            }

            if (section.equals(HOLIDAYS_HEADER)) {
                calendar.addHoliday(parseDate(line));
            } else {
                calendar.addTask(parseTask(line));
            }
        }

        return calendar;
    }

    private Task parseTask(String line) {
        String[] fields = line.split(FIELD_SEPARATOR_REGEX, -1);
        if (fields.length != 5) {
            throw new FileOperationException("Invalid calendar file: malformed task entry: " + line);
        }

        LocalDate date = parseDate(fields[0]);
        LocalTime startTime = parseTime(fields[1]);
        LocalTime endTime = parseTime(fields[2]);
        String name = unescape(fields[3]);
        String note = unescape(fields[4]);

        return new Task(name, note, date, startTime, endTime);
    }

    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new FileOperationException("Invalid calendar file: malformed date: " + value);
        }
    }

    private LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new FileOperationException("Invalid calendar file: malformed time: " + value);
        }
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("|", "\\|")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String unescape(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' && i + 1 < value.length()) {
                char next = value.charAt(i + 1);
                switch (next) {
                    case 'n' -> result.append('\n');
                    case 'r' -> result.append('\r');
                    case '|' -> result.append('|');
                    case '\\' -> result.append('\\');
                    default -> {
                        result.append(c);
                        continue;
                    }
                }
                i++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
