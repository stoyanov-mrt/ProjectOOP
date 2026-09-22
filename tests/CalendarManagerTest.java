package tests;

import exception.HolidayException;
import exception.InvalidHolidayException;
import exception.TaskNotFoundException;
import exception.TaskOverlapException;
import model.Calendar;
import model.Task;
import model.TimeSlot;
import service.CalendarManager;
import tests.support.TestSuite;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static tests.support.TestAssert.assertEquals;
import static tests.support.TestAssert.assertThrows;
import static tests.support.TestAssert.assertTrue;

/**
 * Тестове за {@link CalendarManager}: запазване, отмяна, промяна, търсене,
 * почивни дни, статистика за натовареност и търсене на свободен прозорец.
 * Всички дати се изчисляват спрямо текущата (чрез {@link #nextMonday()}),
 * защото {@link service.TaskValidator} не позволява дати в миналото — така
 * тестовете остават верни независимо кога се изпълняват.
 */
public class CalendarManagerTest implements TestSuite {

    /** Следващият понеделник спрямо днес — гарантирано работен ден с четири работни дни след себе си. */
    private static LocalDate nextMonday() {
        return LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }

    @Override
    public Map<String, Runnable> tests() {
        Map<String, Runnable> tests = new LinkedHashMap<>();
        tests.put("CalendarManager: запазена задача се намира по дата и начален час", this::bookAndFind);
        tests.put("CalendarManager: припокриваща се задача хвърля грешка", this::overlapThrows);
        tests.put("CalendarManager: задача в почивен ден хвърля грешка", this::bookingOnHolidayThrows);
        tests.put("CalendarManager: отмяна на несъществуваща задача хвърля грешка", this::unbookMissingThrows);
        tests.put("CalendarManager: отмяна премахва задачата", this::unbookRemovesTask);
        tests.put("CalendarManager: промяна на име запазва останалите полета (регресия)", this::changeNamePreservesOtherFields);
        tests.put("CalendarManager: неуспешна промяна връща оригиналната задача", this::changeRollsBackOnFailure);
        tests.put("CalendarManager: търсене по ключова дума в име или бележка", this::findByKeyword);
        tests.put("CalendarManager: дневната програма е подредена по начален час", this::agendaIsSorted);
        tests.put("CalendarManager: празничен ден в миналото хвърля грешка", this::holidayInPastThrows);
        tests.put("CalendarManager: ден с вече запазени задачи не може да стане почивен", this::holidayWithTasksThrows);
        tests.put("CalendarManager: статистика за натовареност сумира часовете по дни от седмицата", this::busyDaysAggregates);
        tests.put("CalendarManager: findSlot намира прозорец веднага след сутрешна задача", this::findSlotFindsGapAfterTask);
        tests.put("CalendarManager: findSlot прескача събота и неделя", this::findSlotSkipsWeekend);
        tests.put("CalendarManager: findSlotWith отчита заетостта и на другия календар", this::findSlotWithRespectsOtherCalendar);
        return tests;
    }

    private void bookAndFind() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        Task task = new Task("Среща", "Бележка", date, LocalTime.of(10, 0), LocalTime.of(11, 0));

        manager.bookTask(task);
        Task found = manager.findTaskByDateAndStartTime(date, LocalTime.of(10, 0));

        assertTrue(found != null, "запазената задача трябва да бъде намерена");
        assertEquals("Среща", found.getName(), "намерената задача трябва да е същата, която е запазена");
    }

    private void overlapThrows() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookTask(new Task("Първа", "", date, LocalTime.of(10, 0), LocalTime.of(11, 0)));

        assertThrows(TaskOverlapException.class,
                () -> manager.bookTask(new Task("Втора", "", date, LocalTime.of(10, 30), LocalTime.of(11, 30))),
                "припокриващ се часови диапазон в същия ден трябва да бъде отказан");
    }

    private void bookingOnHolidayThrows() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookHoliday(date);

        assertThrows(HolidayException.class,
                () -> manager.bookTask(new Task("Задача", "", date, LocalTime.of(10, 0), LocalTime.of(11, 0))),
                "не трябва да е възможно запазване на задача в обявен за почивен ден");
    }

    private void unbookMissingThrows() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();

        assertThrows(TaskNotFoundException.class,
                () -> manager.unbookTask(new Task(date, LocalTime.of(10, 0), LocalTime.of(11, 0))),
                "отмяна на несъществуваща задача трябва да хвърли грешка");
    }

    private void unbookRemovesTask() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookTask(new Task("За отмяна", "", date, LocalTime.of(10, 0), LocalTime.of(11, 0)));

        manager.unbookTask(new Task(date, LocalTime.of(10, 0), LocalTime.of(11, 0)));

        assertTrue(manager.findTaskByDateAndStartTime(date, LocalTime.of(10, 0)) == null,
                "след отмяна задачата не трябва да съществува повече");
    }

    private void changeNamePreservesOtherFields() {
        // Тестът покрива конкретна регресия: копиращият конструктор на Task
        // някога пренасяше само името, заради което почти всяка промяна
        // ставаше невалидна (датата/часовете изчезваха).
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookTask(new Task("Старо име", "Бележка", date, LocalTime.of(10, 0), LocalTime.of(11, 0)));

        manager.changeTask(date, LocalTime.of(10, 0), "name", "Ново име");

        Task changed = manager.findTaskByDateAndStartTime(date, LocalTime.of(10, 0));
        assertEquals("Ново име", changed.getName(), "името трябва да е променено");
        assertEquals("Бележка", changed.getNote(), "бележката не трябва да се загуби при промяна на името");
        assertEquals(LocalTime.of(11, 0), changed.getEndTime(), "крайният час не трябва да се загуби при промяна на името");
    }

    private void changeRollsBackOnFailure() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookTask(new Task("Запазена", "", date, LocalTime.of(9, 0), LocalTime.of(10, 0)));
        manager.bookTask(new Task("Пречеща", "", date, LocalTime.of(10, 30), LocalTime.of(11, 30)));

        // удължаваме "Запазена" до 11:00, което я застъпва с "Пречеща" (10:30-11:30)
        assertThrows(TaskOverlapException.class,
                () -> manager.changeTask(date, LocalTime.of(9, 0), "endtime", "11:00"),
                "удължаване на задача върху друга трябва да е отказано");

        Task original = manager.findTaskByDateAndStartTime(date, LocalTime.of(9, 0));
        assertTrue(original != null, "оригиналната задача трябва да остане на мястото си след неуспешна промяна");
        assertEquals(LocalTime.of(10, 0), original.getEndTime(), "крайният час на оригиналната задача не трябва да е променен");
    }

    private void findByKeyword() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookTask(new Task("Среща с клиент", "Важен проект", date, LocalTime.of(10, 0), LocalTime.of(11, 0)));
        manager.bookTask(new Task("Обяд", "лично", date, LocalTime.of(12, 0), LocalTime.of(13, 0)));

        List<Task> found = manager.findTaskByKeyword("ПРОЕКТ");

        assertEquals(1, found.size(), "търсенето трябва да намери точно една задача, без значение от главни/малки букви");
        assertEquals("Среща с клиент", found.get(0).getName(), "намерена трябва да е задачата с 'проект' в бележката");
    }

    private void agendaIsSorted() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookTask(new Task("Късна", "", date, LocalTime.of(15, 0), LocalTime.of(16, 0)));
        manager.bookTask(new Task("Ранна", "", date, LocalTime.of(9, 0), LocalTime.of(10, 0)));

        List<Task> agenda = manager.getAgenda(date);

        assertEquals(2, agenda.size(), "и двете задачи трябва да присъстват");
        assertEquals("Ранна", agenda.get(0).getName(), "по-ранната задача трябва да е първа");
        assertEquals("Късна", agenda.get(1).getName(), "по-късната задача трябва да е втора");
    }

    private void holidayInPastThrows() {
        CalendarManager manager = new CalendarManager();
        assertThrows(InvalidHolidayException.class,
                () -> manager.bookHoliday(LocalDate.now().minusDays(1)),
                "дата в миналото не може да се обяви за почивен ден");
    }

    private void holidayWithTasksThrows() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookTask(new Task("Задача", "", date, LocalTime.of(10, 0), LocalTime.of(11, 0)));

        assertThrows(InvalidHolidayException.class, () -> manager.bookHoliday(date),
                "ден, в който вече има запазени задачи, не може да стане почивен ден");
    }

    private void busyDaysAggregates() {
        CalendarManager manager = new CalendarManager();
        LocalDate monday = nextMonday();
        LocalDate tuesday = monday.plusDays(1);
        manager.bookTask(new Task("Първа", "", monday, LocalTime.of(9, 0), LocalTime.of(11, 0)));
        manager.bookTask(new Task("Втора", "", monday, LocalTime.of(13, 0), LocalTime.of(14, 0)));
        manager.bookTask(new Task("Трета", "", tuesday, LocalTime.of(9, 0), LocalTime.of(10, 0)));

        HashMap<DayOfWeek, Double> stats = manager.busyDays(monday, tuesday);

        assertEquals(3.0, stats.get(DayOfWeek.MONDAY), "понеделник трябва да сумира 2 + 1 = 3 часа");
        assertEquals(1.0, stats.get(DayOfWeek.TUESDAY), "вторник трябва да сумира 1 час");
    }

    private void findSlotFindsGapAfterTask() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        manager.bookTask(new Task("Сутрешна задача", "", date, LocalTime.of(8, 0), LocalTime.of(9, 0)));

        TimeSlot slot = manager.findSlot(date, 1);

        assertTrue(slot != null, "трябва да се намери свободен прозорец");
        assertEquals(date, slot.getDate(), "прозорецът трябва да е на същата дата");
        assertEquals(LocalTime.of(9, 0), slot.getStartTime(), "прозорецът трябва да започне веднага след края на сутрешната задача");
        assertEquals(LocalTime.of(10, 0), slot.getEndTime(), "прозорецът трябва да е с исканата продължителност от 1 час");
    }

    private void findSlotSkipsWeekend() {
        CalendarManager manager = new CalendarManager();
        LocalDate friday = nextMonday().plusDays(4);
        // запълваме целия работен ден в петък, за да принудим търсенето да продължи напред
        manager.bookTask(new Task("Цял ден зает", "", friday, LocalTime.of(8, 0), LocalTime.of(17, 0)));

        TimeSlot slot = manager.findSlot(friday, 1);

        assertTrue(slot != null, "трябва да се намери свободен прозорец следващата седмица");
        assertTrue(slot.getDate().getDayOfWeek() != DayOfWeek.SATURDAY && slot.getDate().getDayOfWeek() != DayOfWeek.SUNDAY,
                "намереният прозорец не трябва да е в събота или неделя");
        assertEquals(friday.plusDays(3), slot.getDate(), "след запълнен петък следващият работен ден е понеделник (петък + 3 дни)");
    }

    private void findSlotWithRespectsOtherCalendar() {
        CalendarManager manager = new CalendarManager();
        LocalDate date = nextMonday();
        // личният календар е свободен цял ден - findSlot сам би върнал 08:00-09:00
        TimeSlot slotAlone = manager.findSlot(date, 1);
        assertEquals(LocalTime.of(8, 0), slotAlone.getStartTime(), "без друг календар прозорецът трябва да е в 08:00");

        Calendar otherCalendar = new Calendar();
        otherCalendar.addTask(new Task("Чужда тренировка", "", date, LocalTime.of(8, 0), LocalTime.of(9, 30)));

        TimeSlot slotWithOther = manager.findSlotWith(date, 1, new ArrayList<>(List.of(otherCalendar)));

        assertTrue(slotWithOther != null, "трябва да се намери прозорец, съвместим и с двата календара");
        assertEquals(LocalTime.of(9, 30), slotWithOther.getStartTime(),
                "прозорецът трябва да е след края на задачата от другия календар (09:30), а не в 08:00");
    }
}
