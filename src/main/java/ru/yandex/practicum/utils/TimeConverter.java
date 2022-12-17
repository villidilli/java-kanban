package ru.yandex.practicum.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm | ZZZZZ");;

    public static String dateTimeToString(ZonedDateTime dateTime) {
        return dateTime.format(formatter);
    }

    public static ZonedDateTime dateTimeFromString(String dateTime) {
        return ZonedDateTime.parse(dateTime, formatter);
    }
}
