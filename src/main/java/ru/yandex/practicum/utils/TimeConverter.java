package ru.yandex.practicum.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static ru.yandex.practicum.tasks.Task.UNREACHEBLE_DATE;

public class TimeConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm | ZZZZZ");;

    public static String dateTimeToString(ZonedDateTime dateTime) {
        if (dateTime == UNREACHEBLE_DATE) {
            return "";
        }
        return dateTime.format(formatter);
    }

    public static ZonedDateTime dateTimeFromString(String dateTime) {
        return ZonedDateTime.parse(dateTime, formatter);
    }
}
