package cli.commands;

import cli.MenuPrinter;

public class PrintMenuCommand implements Command {
    private MenuPrinter printer;

    public PrintMenuCommand(MenuPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void execute(String[] args) {
        printer.printMainMenu();
    }
    @Override
    public String getName() {
        return "printMenu";
    }
}
