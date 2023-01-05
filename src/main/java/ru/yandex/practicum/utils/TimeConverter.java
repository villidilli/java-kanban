package ru.yandex.practicum.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static ru.yandex.practicum.tasks.Task.UNREACHEBLE_DATE;

public class TimeConverter extends TypeAdapter<ZonedDateTime>{
    protected static final DateTimeFormatter ZONED_DATE_TIME_FORMATTER =
                                            DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm | ZZZZZ");

    public static String dateTimeToString(ZonedDateTime dateTime) {
        if (dateTime == UNREACHEBLE_DATE) {
            return null;
        }
        return dateTime.format(ZONED_DATE_TIME_FORMATTER);
    }

    public static ZonedDateTime dateTimeFromString(String dateTime) {
        if (dateTime.equals("null")) {
            return UNREACHEBLE_DATE;
        }
        return ZonedDateTime.parse(dateTime, ZONED_DATE_TIME_FORMATTER);
    }

    @Override
    public void write(final JsonWriter jsonWriter, final ZonedDateTime zonedDateTime) throws IOException {
        if (zonedDateTime == UNREACHEBLE_DATE) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(zonedDateTime.format(ZONED_DATE_TIME_FORMATTER));
        }

    }

    @Override
    public ZonedDateTime read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.nextString() == null) {
            return UNREACHEBLE_DATE;
        }
        return ZonedDateTime.parse(jsonReader.nextString(), ZONED_DATE_TIME_FORMATTER);
    }
}