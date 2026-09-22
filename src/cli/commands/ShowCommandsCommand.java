package cli.commands;

import cli.MenuPrinter;

/** {@code commands} — prints detailed descriptions of the calendar-specific commands. */
public class ShowCommandsCommand implements Command{
    private MenuPrinter printer;

    public ShowCommandsCommand(MenuPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void execute(String[] args) {
        printer.printCommands();
    }
    @Override
    public String getName() {
        return "commands";
    }
    @Override
    public boolean requiresOpenCalendar() {
        return false;
    }
}
