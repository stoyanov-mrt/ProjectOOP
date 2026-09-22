package tests.support;

import java.util.Objects;

/**
 * Минимален помощен клас за проверки в тестовете — без външни библиотеки,
 * само с {@code javac}/{@code java}. Всеки метод хвърля {@link AssertionError}
 * с описание на български, ако проверката е неуспешна.
 */
public final class TestAssert {

    private TestAssert() {
    }

    /** Проверява, че {@code expected} и {@code actual} са равни (по {@link Objects#equals}). */
    public static void assertEquals(Object expected, Object actual, String description) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(description + " -> очаквано: " + expected + ", получено: " + actual);
        }
    }

    /** Проверява, че {@code condition} е истина. */
    public static void assertTrue(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
    }

    /**
     * Изпълнява {@code action} и очаква то да хвърли изключение от тип
     * {@code expectedType} (или негов подклас). Проваля се, ако не бъде
     * хвърлено нищо или бъде хвърлено изключение от друг тип.
     */
    public static void assertThrows(Class<? extends Throwable> expectedType, Runnable action, String description) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (expectedType.isInstance(actual)) {
                return;
            }
            throw new AssertionError(description + " -> очакван клас изключение: " + expectedType.getSimpleName()
                    + ", получен: " + actual.getClass().getSimpleName());
        }
        throw new AssertionError(description + " -> очакваше се изключение от тип " + expectedType.getSimpleName()
                + ", но не беше хвърлено нищо");
    }
}
