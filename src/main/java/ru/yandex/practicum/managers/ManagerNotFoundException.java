package ru.yandex.practicum.managers;

public class ManagerNotFoundException extends RuntimeException {
    public static final String NOT_TRANSFERRED_INPUT = System.lineSeparator() + "ERROR -> [объект не передан]";
    public static final String NOT_FOUND_BY_ID = System.lineSeparator() +
            "ERROR -> [объект с указанным ID не найден]";
    public static final String NOT_FOUND_PREV_VERSION = System.lineSeparator() +
            "ERROR -> [предыдущая версия задачи не найдена]";
    public static final String NOT_FOUND_PARENT = System.lineSeparator() +
            "ERROR -> [не найден родительский объект с указанным ID]";

    public ManagerNotFoundException(String message) {
        super(message);
    }
}
