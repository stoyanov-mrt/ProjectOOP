package tests;

import exception.InvalidDateFormatException;
import model.Task;
import service.TaskValidator;
import tests.support.TestSuite;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static tests.support.TestAssert.assertThrows;

/**
 * Тестове за {@link TaskValidator}: проверка на датата, часовия диапазон и
 * името на задачата.
 */
public class TaskValidatorTest implements TestSuite {
    private final TaskValidator validator = new TaskValidator();

    @Override
    public Map<String, Runnable> tests() {
        Map<String, Runnable> tests = new LinkedHashMap<>();
        tests.put("TaskValidator: валидна задача преминава без грешка", this::validTaskPasses);
        tests.put("TaskValidator: дата в миналото хвърля грешка", this::pastDateThrows);
        tests.put("TaskValidator: липсваща дата хвърля грешка", this::nullDateThrows);
        tests.put("TaskValidator: краен час преди началния хвърля грешка", this::endBeforeStartThrows);
        tests.put("TaskValidator: еднакъв начален и краен час хвърля грешка", this::equalStartAndEndThrows);
        tests.put("TaskValidator: липсващо име хвърля грешка", this::nullNameThrows);
        tests.put("TaskValidator: празно име хвърля грешка", this::emptyNameThrows);
        return tests;
    }

    private Task futureTask(String name) {
        return new Task(name, "Бележка", LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    private void validTaskPasses() {
        validator.validateTask(futureTask("Валидна задача"));
    }

    private void pastDateThrows() {
        Task task = new Task("Задача в миналото", "", LocalDate.now().minusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertThrows(InvalidDateFormatException.class, () -> validator.validateTask(task),
                "дата преди днешната трябва да е невалидна");
    }

    private void nullDateThrows() {
        Task task = new Task("Задача без дата", "", null, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertThrows(InvalidDateFormatException.class, () -> validator.validateTask(task),
                "липсваща дата трябва да е невалидна");
    }

    private void endBeforeStartThrows() {
        Task task = new Task("Обърнати часове", "", LocalDate.now().plusDays(1), LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertThrows(InvalidDateFormatException.class, () -> validator.validateTask(task),
                "краен час преди началния трябва да е невалиден");
    }

    private void equalStartAndEndThrows() {
        Task task = new Task("Нулева продължителност", "", LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(10, 0));
        assertThrows(InvalidDateFormatException.class, () -> validator.validateTask(task),
                "еднакъв начален и краен час трябва да е невалиден");
    }

    private void nullNameThrows() {
        Task task = new Task(null, "", LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertThrows(InvalidDateFormatException.class, () -> validator.validateTask(task),
                "липсващо име трябва да е невалидно");
    }

    private void emptyNameThrows() {
        Task task = futureTask("");
        assertThrows(InvalidDateFormatException.class, () -> validator.validateTask(task),
                "празно име трябва да е невалидно");
    }
}
