package cli;

import exception.CalendarException;

import java.util.Scanner;

public class Main {
    protected Scanner sc = new Scanner(System.in);
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