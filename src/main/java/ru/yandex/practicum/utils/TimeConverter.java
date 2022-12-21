package ru.yandex.practicum.utils;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static ru.yandex.practicum.tasks.Task.UNREACHEBLE_DATE;

public class TimeConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm | ZZZZZ");;

    public static String dateTimeToString(ZonedDateTime dateTime) {
        if (dateTime == UNREACHEBLE_DATE) {
            return null;
        }
        return dateTime.format(formatter);
    }

    public static ZonedDateTime dateTimeFromString(String dateTime) {
        if (dateTime.equals("null")) {
            return UNREACHEBLE_DATE;
        }
        return ZonedDateTime.parse(dateTime, formatter);
    }

    public static Duration durationFromString(String duration) {
        if (duration.equals("0")) {
            return Duration.ZERO;
        }
        return Duration.ofMinutes(Long.parseLong(duration));
    }
}
