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

/**
 * Parses each line of user input into a command name and arguments,
 * looks up the matching {@link Command} by name, and runs it. Also
 * enforces the rule that every command besides
 * {@code help}/{@code commands}/{@code open}/{@code exit} requires a
 * calendar file to already be open (see {@link Command#requiresOpenCalendar()}).
 */
public class CommandLineDispatcher {
    /** Matches either a {@code "quoted phrase"} or a run of non-space characters. */
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\"([^\"]*)\"|(\\S+)");

    private Map<String, Command> commands = new HashMap<>();

    MenuPrinter menuPrinter = new MenuPrinter();
    CalendarManager calendarManager = new CalendarManager();
    DateParser dateParser = new DateParser();

    /**
     * Wires up every supported {@link Command} and their shared
     * dependencies (calendar state, date parsing, file I/O).
     *
     * @param scanner the shared input scanner, passed through to
     *                {@link MergeCommand} for its interactive conflict prompts
     */
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

    /**
     * Tokenizes {@code input}, resolves its first token to a registered
     * {@link Command}, and executes it.
     *
     * @throws InvalidCommandException if the command name is unrecognized,
     *         or the command requires an open calendar and none is open
     */
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

    /**
     * Splits {@code input} on whitespace, treating a {@code "quoted phrase"}
     * as a single token so multi-word arguments (a task name, a note, a
     * file path with spaces) can be passed without ambiguity.
     */
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
