package cli.commands;

import cli.MenuPrinter;

public class HelpCommand implements Command{
    private MenuPrinter printer;

    public HelpCommand(MenuPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void execute(String[] args) {
        printer.printHelp();
    }
    @Override
    public String getName() {
        return "help";
    }
    @Override
    public boolean requiresOpenCalendar() {
        return false;
    }

}
