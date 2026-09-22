package tests;

import model.Task;
import tests.support.TestSuite;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static tests.support.TestAssert.assertEquals;
import static tests.support.TestAssert.assertTrue;

/**
 * Тестове за {@link Task}: копиращият конструктор, {@code equals} и
 * подредбата ({@code compareTo}).
 */
public class TaskTest implements TestSuite {

    @Override
    public Map<String, Runnable> tests() {
        Map<String, Runnable> tests = new LinkedHashMap<>();
        tests.put("Task: копиращият конструктор пренася всички полета", this::copyConstructorCopiesAllFields);
        tests.put("Task: equals сравнява само дата, начален и краен час", this::equalsComparesOnlyDateAndTimes);
        tests.put("Task: compareTo подрежда първо по дата, после по начален час", this::compareToOrdersByDateThenStartTime);
        return tests;
    }

    private void copyConstructorCopiesAllFields() {
        Task original = new Task("Име", "Бележка", LocalDate.of(2026, 9, 25), LocalTime.of(14, 0), LocalTime.of(15, 0));

        Task copy = new Task(original);

        assertEquals(original.getName(), copy.getName(), "името трябва да се копира");
        assertEquals(original.getNote(), copy.getNote(), "бележката трябва да се копира");
        assertEquals(original.getDate(), copy.getDate(), "датата трябва да се копира");
        assertEquals(original.getStartTime(), copy.getStartTime(), "началният час трябва да се копира");
        assertEquals(original.getEndTime(), copy.getEndTime(), "крайният час трябва да се копира");
    }

    private void equalsComparesOnlyDateAndTimes() {
        Task a = new Task("Първо име", "Първа бележка", LocalDate.of(2026, 9, 25), LocalTime.of(14, 0), LocalTime.of(15, 0));
        Task b = new Task("Второ име", "Втора бележка", LocalDate.of(2026, 9, 25), LocalTime.of(14, 0), LocalTime.of(15, 0));

        assertTrue(a.equals(b), "две задачи с еднакви дата/начален/краен час трябва да са равни, дори с различни имена");
    }

    private void compareToOrdersByDateThenStartTime() {
        Task earlierDateLaterTime = new Task("A", "", LocalDate.of(2026, 9, 24), LocalTime.of(18, 0), LocalTime.of(19, 0));
        Task laterDateEarlierTime = new Task("B", "", LocalDate.of(2026, 9, 25), LocalTime.of(8, 0), LocalTime.of(9, 0));

        assertTrue(earlierDateLaterTime.compareTo(laterDateEarlierTime) < 0,
                "задача от по-ранна дата трябва да е 'по-малка', дори ако часът ѝ е по-късен от този на другата задача");
    }
}
