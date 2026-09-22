import cli.Main;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Entry point for the Personal Calendar command-line application.
 */
public class CalendarSystem {
    public static void main(String[] args) {
        // Names/notes may contain Cyrillic text; force UTF-8 for console
        // output regardless of the host console's default codepage
        // (e.g. Cp1251 on a Bulgarian-locale Windows console), so it
        // displays correctly everywhere - including IntelliJ's run console.
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        Main main = new Main();
        main.run();
    }
}
