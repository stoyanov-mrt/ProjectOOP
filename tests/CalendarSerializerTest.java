package tests;

import exception.FileOperationException;
import model.Calendar;
import model.Task;
import persistence.CalendarSerializer;
import tests.support.TestSuite;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static tests.support.TestAssert.assertEquals;
import static tests.support.TestAssert.assertThrows;
import static tests.support.TestAssert.assertTrue;

/**
 * Тестове за {@link CalendarSerializer}: обратим запис/прочит
 * (round-trip) на задачи и почивни дни, откриване на повреден файл, и
 * коректно "escape"-ване на специални символи в името/бележката.
 */
public class CalendarSerializerTest implements TestSuite {
    private final CalendarSerializer serializer = new CalendarSerializer();

    @Override
    public Map<String, Runnable> tests() {
        Map<String, Runnable> tests = new LinkedHashMap<>();
        tests.put("CalendarSerializer: сериализация и десериализация връщат същите данни", this::roundTripPreservesData);
        tests.put("CalendarSerializer: име/бележка със знак '|' се възстановяват правилно", this::escapesPipeCharacter);
        tests.put("CalendarSerializer: липсващ секционен хедър хвърля грешка", this::missingHeaderThrows);
        tests.put("CalendarSerializer: ред със грешен брой полета хвърля грешка", this::malformedTaskLineThrows);
        tests.put("CalendarSerializer: невалидна дата във файла хвърля грешка", this::malformedDateThrows);
        return tests;
    }

    private void roundTripPreservesData() {
        Calendar original = new Calendar();
        original.addHoliday(LocalDate.of(2026, 10, 2));
        original.addTask(new Task("Среща", "Бележка", LocalDate.of(2026, 9, 25), LocalTime.of(14, 0), LocalTime.of(15, 0)));

        Calendar restored = serializer.deserialize(serializer.serialize(original));

        assertEquals(1, restored.getTasks().size(), "трябва да има точно една възстановена задача");
        assertEquals("Среща", restored.getTasks().get(0).getName(), "името трябва да се възстанови непроменено");
        assertTrue(restored.getHolidays().contains(LocalDate.of(2026, 10, 2)), "почивният ден трябва да се възстанови");
    }

    private void escapesPipeCharacter() {
        Calendar original = new Calendar();
        original.addTask(new Task("Име с | символ", "Бележка с | символ", LocalDate.of(2026, 9, 25), LocalTime.of(9, 0), LocalTime.of(10, 0)));

        Calendar restored = serializer.deserialize(serializer.serialize(original));

        assertEquals("Име с | символ", restored.getTasks().get(0).getName(), "символът '|' в името трябва да преживее запис/прочит");
        assertEquals("Бележка с | символ", restored.getTasks().get(0).getNote(), "символът '|' в бележката трябва да преживее запис/прочит");
    }

    private void missingHeaderThrows() {
        assertThrows(FileOperationException.class, () -> serializer.deserialize("2026-09-25|09:00|10:00|Име|Бележка"),
                "ред извън секция #HOLIDAYS/#TASKS трябва да се третира като повреден файл");
    }

    private void malformedTaskLineThrows() {
        assertThrows(FileOperationException.class, () -> serializer.deserialize("#TASKS\n2026-09-25|09:00|10:00|Само три полета"),
                "ред с грешен брой полета трябва да се третира като повреден файл");
    }

    private void malformedDateThrows() {
        assertThrows(FileOperationException.class, () -> serializer.deserialize("#TASKS\nне-е-дата|09:00|10:00|Име|Бележка"),
                "невалидна дата във файла трябва да се третира като повреден файл");
    }
}
