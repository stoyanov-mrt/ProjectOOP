package tests;

import exception.InvalidDateFormatException;
import exception.InvalidTimeFormatException;
import parser.DateParser;
import tests.support.TestSuite;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static tests.support.TestAssert.assertEquals;
import static tests.support.TestAssert.assertThrows;

/**
 * Тестове за {@link DateParser}: двата поддържани формата на дата, форматът
 * на часа и обработката на невалиден вход.
 */
public class DateParserTest implements TestSuite {
    private final DateParser parser = new DateParser();

    @Override
    public Map<String, Runnable> tests() {
        Map<String, Runnable> tests = new LinkedHashMap<>();
        tests.put("DateParser: разпознава формат dd-MM-yyyy", this::parsesDayMonthYear);
        tests.put("DateParser: разпознава формат yyyy-MM-dd", this::parsesYearMonthDay);
        tests.put("DateParser: невалиден формат на дата хвърля грешка", this::invalidDateThrows);
        tests.put("DateParser: разпознава час във формат H:mm", this::parsesTime);
        tests.put("DateParser: невалиден формат на час хвърля грешка", this::invalidTimeThrows);
        return tests;
    }

    private void parsesDayMonthYear() {
        assertEquals(LocalDate.of(2026, 9, 25), parser.parseDate("25-09-2026"), "25-09-2026 трябва да се разпознае");
    }

    private void parsesYearMonthDay() {
        assertEquals(LocalDate.of(2026, 9, 25), parser.parseDate("2026-09-25"), "2026-09-25 трябва да се разпознае");
    }

    private void invalidDateThrows() {
        assertThrows(InvalidDateFormatException.class, () -> parser.parseDate("не-е-дата"),
                "безсмислен низ не трябва да се приема за дата");
    }

    private void parsesTime() {
        assertEquals(LocalTime.of(9, 30), parser.parseTime("9:30"), "9:30 трябва да се разпознае");
    }

    private void invalidTimeThrows() {
        assertThrows(InvalidTimeFormatException.class, () -> parser.parseTime("абв"),
                "безсмислен низ не трябва да се приема за час");
    }
}
