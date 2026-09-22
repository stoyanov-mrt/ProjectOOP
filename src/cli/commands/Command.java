package cli.commands;

/**
 * A single CLI command, identified by its name (e.g. {@code "book"}) and
 * registered with {@link cli.CommandLineDispatcher}.
 */
public interface Command {
    /** @return the command's name as typed by the user, e.g. {@code "book"} */
    String getName();

    /**
     * Runs the command.
     *
     * @param args the tokenized input line; {@code args[0]} is the command
     *             name itself, {@code args[1..]} are its arguments
     */
    void execute(String[] args);

    /**
     * @return whether this command may only run after a calendar file has
     *         been successfully opened. True for every command except
     *         {@code open}, {@code help}, {@code commands}, and {@code exit}.
     */
    default boolean requiresOpenCalendar() {
        return true;
    }
}
