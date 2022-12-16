package ru.yandex.practicum.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {
    private static DateTimeFormatter formatter;

    public static String dateTimeToString(ZonedDateTime dateTime) {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm | ZZZZZ");
        return dateTime.format(formatter);
    }
}
