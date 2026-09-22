package service;

import exception.FileOperationException;
import model.Calendar;
import persistence.CalendarSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Reads and writes {@link Calendar} data to disk, and tracks which file is
 * currently open so plain {@code save} can write back to it.
 */
public class FileManager {
    private String currentFileName;
    private final CalendarSerializer calendarSerializer;

    public FileManager(CalendarSerializer calendarSerializer) {
        this.calendarSerializer = calendarSerializer;
    }

    /**
     * Opens {@code fileName} as the current file. If it does not exist yet,
     * a new empty calendar is returned instead of raising an error.
     */
    public Calendar open(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            setCurrentFileName(fileName);
            return new Calendar();
        }

        Calendar calendar = calendarSerializer.deserialize(readFile(file));
        setCurrentFileName(fileName);
        return calendar;
    }

    /**
     * Reads and parses {@code fileName} without changing the currently open
     * file. Used by commands that need to look at another calendar file
     * (e.g. {@code findslotwith}, {@code merge}).
     */
    public Calendar peek(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileOperationException("File not found: " + fileName);
        }
        return calendarSerializer.deserialize(readFile(file));
    }

    public void save(Calendar calendar) {
        if (currentFileName == null) {
            throw new FileOperationException("No file is currently open");
        }
        writeToFile(calendar, currentFileName);
    }

    public void saveAs(Calendar calendar, String fileName) {
        writeToFile(calendar, fileName);
        setCurrentFileName(fileName);
    }

    public void close() {
        this.currentFileName = null;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    private String readFile(File file) {
        StringBuilder content = new StringBuilder();
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                content.append(reader.nextLine());
                content.append(System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            throw new FileOperationException("File not found: " + file.getName());
        }
        return content.toString();
    }

    private void writeToFile(Calendar calendar, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(calendarSerializer.serialize(calendar));
        } catch (IOException e) {
            throw new FileOperationException("Could not write to file: " + fileName);
        }
    }
}
