package ru.yandex.practicum.managers;

public class ManagerSaveException extends RuntimeException {
    public static final String NOT_READ_FILE = System.lineSeparator() + "ERROR -> [файл не прочитан]";
    public static final String NOT_WRITE_FILE = System.lineSeparator() + "ERROR -> [проблема записи в файл]";
    public static final String NOT_SAVE = System.lineSeparator() + "ERROR -> [объекты не сохранены]";

    public ManagerSaveException(String message) {
        super(message);
    }
}