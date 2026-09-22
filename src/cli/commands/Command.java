package cli.commands;

public interface Command {
    String getName();
    void execute(String[] args);

    default boolean requiresOpenCalendar() {
        return true;
    }
}
