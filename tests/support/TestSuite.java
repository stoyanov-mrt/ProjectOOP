package tests.support;

import java.util.Map;

/**
 * Набор от именувани тестови сценарии. Всеки ключ на картата е описание на
 * сценария на български, показвано от {@link tests.TestRunner}; стойността
 * е самият тест — метод, който хвърля {@link AssertionError} (или друго
 * изключение) при неуспех.
 */
public interface TestSuite {
    Map<String, Runnable> tests();
}
