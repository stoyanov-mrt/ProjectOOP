package cli.commands;

public class ExitCommand implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println("Exiting...");
        System.exit(0);
    }
    @Override
    public String getName() {
        return "exit";
    }
    @Override
    public boolean requiresOpenCalendar() {
        return false;
    }
}
