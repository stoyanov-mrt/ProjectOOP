package cli;

/**
 * Prints the static, user-facing text for the main menu, the {@code help}
 * command, and the {@code commands} command. Holds no state or logic of
 * its own.
 */
public class MenuPrinter {
    /** Prints the one-line-per-command menu shown once at startup. */
    public void printMainMenu() {
        System.out.println("open <file>. Open a calendar file");
        System.out.println("close. Close the currently open file");
        System.out.println("save. Save changes back to the open file");
        System.out.println("saveas <file>. Save changes to a new file");
        System.out.println("help. Help");
        System.out.println("commands. Show detailed command descriptions");
        System.out.println("book. Book a task");
        System.out.println("unbook. Unbook a task");
        System.out.println("agenda. Show tasks for a day");
        System.out.println("change. Change a task");
        System.out.println("find. Find a task by keyword");
        System.out.println("holiday. Mark a date as a holiday");
        System.out.println("busydays. Display busy days");
        System.out.println("findslot. Find a free window");
        System.out.println("findslotwith. Find a free window that also fits another calendar");
        System.out.println("merge. Merge a calendar from a file into the current calendar");
        System.out.println("exit. Exit program");
        System.out.println("-----------------");
        System.out.println("Enter choice: ");
    }
    /** Prints the description of the common file/program commands, for {@code help}. */
    public void printHelp() {
        System.out.println("--------------------");
        System.out.println("The following commands are supported:");
        System.out.println("open <file>      opens <file>");
        System.out.println("close            closes currently opened file");
        System.out.println("save             saves the currently open file");
        System.out.println("saveas <file>    saves the currently open file in <file>");
        System.out.println("help             prints this information");
        System.out.println("commands         prints more specific commands");
        System.out.println("exit             exits the program");
        System.out.println("--------------------");

    }
    /** Prints the detailed description of every calendar-specific command, for {@code commands}. */
    public void printCommands() {
        System.out.println("--------------------");
        System.out.println("book <date> <starttime> <endtime> <name> <note> - Запазва час за среща с име <name> и коментар <note>\n на дата <date>" +
                "с начален час <starttime> и краен час <endtime>.");
        System.out.println("--------------------");
        System.out.println("unbook <date> <starttime> <endtime> - Отменя час за среща на дата <date>\n с начален час <starttime> и" +
                "краен час <endtime>.");
        System.out.println("--------------------");
        System.out.println("agenda <date> - Извежда хронологичен списък с всички ангажименти за деня <date>.");
        System.out.println("--------------------");
        System.out.println("change <date> <startime> <endtime> <option> <newvalue> - <option> е едно от date, starttime, enddate, name, note.\n" +
                "Задава нова стойност <newvalue> на събитието на дата <date> с\n" +
                "начален час <starttime>, като при промяна на дата и час се прави\n" +
                "проверка дали са коректни и свободни.");
        System.out.println("--------------------");
        System.out.println("find <string> - Търсене на среща: извеждат се данните за всички срещи, в чието име\n" +
                "или бележка се съдържа низът <string>.");
        System.out.println("--------------------");
        System.out.println("holiday <date> - Датата <date> се отбелязва като неработна.");
        System.out.println("--------------------");
        System.out.println("busydays <from> <to> - Извеждане на статистика за натовареност: по дадени начална дата\n" +
                "<from> и крайна дата <to> се извежда списък с дните от седмицата,\n" +
                "подредени по критерия “брой заети часове”.");
        System.out.println("--------------------");
        System.out.println("findslot <fromdate> <hours> - Намиране на свободно място за среща: по дадена дата\n" +
                "<fromdate> и желана продължителност на срещата <hours> търси дата,\n" +
                "на която е възможно да се запази такава среща, но само в работни\n" +
                "дни и не преди 8 часа или след 17 часа.");
        System.out.println("--------------------");
        System.out.println("findslotwith <fromdate> <hours> <calendar> [<calendar> ...] - Намиране на свободно място за среща, синхронизирана с даден\n" +
                "календар: по дадена дата <fromdate> и желана продължителност на\n" +
                "срещата <hours> търси дата, на която е възможно да се запази такава\n" +
                "среща в текущия календар и в календара, записан във файл\n" +
                "<calendar>, но само в работни дни и не преди 8 часа или след 17\n" +
                "часа.");
        System.out.println("--------------------");
        System.out.println("merge <calendar> [<calendar> ...] - Прехвърля всички събития от календара, записан във файл <calendar>, в текущия календар. Прехвърлянето става в диалогов\n" +
                "режим така, че ако има конфликт на събития потребителят има\n" +
                "възможност да избере кое събитие да остане и кое да се премести в\n" +
                "друг ден и час.\n");

        System.out.println("--------------------");

    }
}
