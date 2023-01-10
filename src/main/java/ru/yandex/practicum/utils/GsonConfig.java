package ru.yandex.practicum.utils;

import com.google.gson.*;

import ru.yandex.practicum.tasks.*;

import java.time.Duration;
import java.time.ZonedDateTime;

public class GsonConfig {
    public static Gson getGsonConverterConfig() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Status.class, new StatusConverter())
                .registerTypeAdapter(ZonedDateTime.class, new DateTimeConverter())
                .registerTypeAdapter(Duration.class, new DurationConverter())
                .create();
    }

    public static Gson getGsonTaskConfig() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Status.class, new StatusConverter())
                .registerTypeAdapter(ZonedDateTime.class, new DateTimeConverter())
                .registerTypeAdapter(Duration.class, new DurationConverter())
                .registerTypeAdapter(Task.class, new TaskToJsonConverter())
                .registerTypeAdapter(SubTask.class, new SubtaskToJsonConverter())
                .registerTypeAdapter(Epic.class, new EpicToJsonConverter())
                .create();
    }
}