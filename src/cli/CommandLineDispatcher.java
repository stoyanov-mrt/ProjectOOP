package cli;

import cli.commands.*;
import exception.InvalidCommandException;

import parser.DateParser;
import persistence.CalendarSerializer;
import service.CalendarManager;
import service.FileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineDispatcher {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\"([^\"]*)\"|(\\S+)");

    private Map<String, Command> commands = new HashMap<>();

    MenuPrinter menuPrinter = new MenuPrinter();
    CalendarManager calendarManager = new CalendarManager();
    DateParser dateParser = new DateParser();

    public CommandLineDispatcher(Scanner scanner) {
        CalendarSerializer calendarSerializer = new CalendarSerializer();
        FileManager fileManager = new FileManager(calendarSerializer);

        registerCommand(new HelpCommand(menuPrinter));
        registerCommand(new ShowCommandsCommand(menuPrinter));
        registerCommand(new ExitCommand());
        registerCommand(new OpenCommand(fileManager, calendarManager));
        registerCommand(new CloseCommand(calendarManager, fileManager));
        registerCommand(new SaveCommand(calendarManager, fileManager));
        registerCommand(new SaveAsCommand(calendarManager, fileManager));
        registerCommand(new BookCommand(dateParser, calendarManager));
        registerCommand(new UnbookCommand(dateParser, calendarManager));
        registerCommand(new AgendaCommand(dateParser, calendarManager));
        registerCommand(new ChangeCommand(dateParser, calendarManager));
        registerCommand(new FindCommand(calendarManager));
        registerCommand(new HolidayCommand(calendarManager, dateParser));
        registerCommand(new BusydaysCommand(calendarManager, dateParser));
        registerCommand(new FindSlotCommand(calendarManager, dateParser));
        registerCommand(new FindSlotWithCommand(calendarManager, dateParser, fileManager));
        registerCommand(new MergeCommand(calendarManager, fileManager, dateParser, scanner));
    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void doCommand(String input) {
        if (input == null || input.isBlank()) {
            return;
        }

        String[] tokens = tokenize(input);
        String commandName = tokens[0].toLowerCase();

        Command command = commands.get(commandName);

        if (command == null) {
            throw new InvalidCommandException("Unknown command: " + commandName);
        }

        if (command.requiresOpenCalendar() && !calendarManager.isOpen()) {
            throw new InvalidCommandException("No file is open. Use 'open <file>' first.");
        }

        command.execute(tokens);
    }

    private String[] tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(matcher.group(1));
            } else {
                tokens.add(matcher.group(2));
            }
        }

        return tokens.toArray(new String[0]);
    }

    public void printMenu() {
        menuPrinter.printMainMenu();
    }

}
