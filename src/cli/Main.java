package cli;

import exception.CalendarException;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Runs the interactive read-eval-print loop: prints the main menu once,
 * then repeatedly reads a line from standard input and hands it to the
 * {@link CommandLineDispatcher}. Every {@link CalendarException} is caught
 * and shown as {@code "Error: ..."} without stopping the loop; any other
 * exception is shown as {@code "Unexpected error: ..."}.
 */
public class Main {
    protected Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
    protected CommandLineDispatcher cld = new CommandLineDispatcher(sc);

    public void run() {
        boolean isRunning = true;

        cld.printMenu();

        while (isRunning) {
            System.out.print("\nPlease enter your choice: ");
            try {
                String input = sc.nextLine().trim();
                cld.doCommand(input);
            } catch (CalendarException e) {
                System.out.println("Error: " + e.getMessage());
            }
            catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }

        }

    }

}