package ru.yandex.practicum.tasks;

public class TimeValueException extends RuntimeException {
    public static final String INTERVAL_INTERSECTION = System.lineSeparator() +
            "ERROR -> [Пересечение интервалов выполнения]";
    public static final String POSITIVE_DURATION = "Значение продолжительности должно быть больше 0";
    public TimeValueException(String message) {
        super(message);
    }
}