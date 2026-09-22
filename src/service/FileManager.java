package service;

import exception.FileOperationException;
import model.Calendar;
import persistence.CalendarSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileManager {
    private String currentFileName;
    private CalendarSerializer calendarSerializer;

    public FileManager(CalendarSerializer calendarSerializer) {
        this.calendarSerializer = calendarSerializer;
    }


    public Calendar open(String fileName) {
        File file = new File(fileName);

        StringBuilder content = new StringBuilder();

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                content.append(reader.nextLine());
                content.append(System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            throw new FileOperationException("File not found: " + fileName);
        }

        setCurrentFileName(fileName);

        return calendarSerializer.deserialize(content.toString());
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

//    public void save(Calendar calendar) { ... }
//
//    public void saveAs(Calendar calendar, String fileName) { ... }
//
//    public void close() { ... }

}
