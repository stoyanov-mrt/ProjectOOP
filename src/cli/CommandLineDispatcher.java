package cli;

import cli.commands.*;
import exception.InvalidCommandException;

import parser.DateParser;
import service.CalendarManager;

import java.util.HashMap;
import java.util.Map;

public class CommandLineDispatcher {
    private Map<String, Command> commands = new HashMap<>();

    MenuPrinter menuPrinter = new MenuPrinter();
    CalendarManager calendarManager = new CalendarManager();
    DateParser dateParser = new DateParser();

    public CommandLineDispatcher() {
        registerCommand(new HelpCommand(menuPrinter));
        registerCommand(new ShowCommandsCommand(menuPrinter));
        registerCommand(new ExitCommand());
        registerCommand(new BookCommand(dateParser, calendarManager));
        registerCommand(new UnbookCommand(dateParser, calendarManager));
        registerCommand(new AgendaCommand(dateParser, calendarManager));
        registerCommand(new ChangeCommand(dateParser, calendarManager));
        registerCommand(new FindCommand(calendarManager));
        registerCommand(new BookHolidayCommand(calendarManager, dateParser));
        registerCommand(new BusydaysCommand(calendarManager, dateParser));
        registerCommand(new FindSlotCommand(calendarManager, dateParser));
    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void doCommand(String input) {
        if (input == null || input.isBlank()) {
            return;
        }

        String[] tokens = input.split("\\s+");
        String commandName = tokens[0].toLowerCase();

        Command command = commands.get(commandName);

        if (command == null) {
            throw new InvalidCommandException("Unknown command: " + commandName);
        }

        command.execute(tokens);
    }

    public void printMenu() {
        menuPrinter.printMainMenu();
    }

}






