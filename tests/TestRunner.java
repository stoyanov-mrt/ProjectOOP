package tests;

import tests.support.TestSuite;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Изпълнява всички тестови класове в пакет {@code tests} и извежда резултата
 * на български: "[OK]" за успешен сценарий, "[ГРЕШКА]" за неуспешен, и
 * обобщение накрая. Излиза с код 1, ако поне един сценарий е неуспешен —
 * удобно за проверка в скрипт/CI без да се чете целия изход.
 */
public class TestRunner {

    private record NamedTest(String description, Runnable action) {
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        List<NamedTest> allTests = new ArrayList<>();
        collect(allTests, new TaskTest());
        collect(allTests, new TaskValidatorTest());
        collect(allTests, new DateParserTest());
        collect(allTests, new CalendarSerializerTest());
        collect(allTests, new CalendarManagerTest());

        int passed = 0;
        int failed = 0;

        System.out.println("Стартиране на тестовете за проект \"Личен календар\"...");
        System.out.println("========================================================");

        for (NamedTest test : allTests) {
            try {
                test.action().run();
                System.out.println("[OK]     " + test.description());
                passed++;
            } catch (Throwable failure) {
                System.out.println("[ГРЕШКА] " + test.description());
                System.out.println("         -> " + failure.getMessage());
                failed++;
            }
        }

        System.out.println("========================================================");
        System.out.println("Резултат: " + passed + " успешни / " + failed + " неуспешни (общо " + allTests.size() + ")");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void collect(List<NamedTest> allTests, TestSuite suite) {
        for (Map.Entry<String, Runnable> entry : suite.tests().entrySet()) {
            allTests.add(new NamedTest(entry.getKey(), entry.getValue()));
        }
    }
}
