package cli.commands;

import cli.MenuPrinter;

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
}
