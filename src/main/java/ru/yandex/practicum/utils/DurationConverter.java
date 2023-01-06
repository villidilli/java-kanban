package ru.yandex.practicum.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationConverter extends TypeAdapter<Long> {
    public static long durationFromString(String duration) {
        return Long.parseLong(duration);
    }

    @Override
    public void write(JsonWriter jsonWriter, Long duration) throws IOException {
        jsonWriter.value(duration);
    }

    @Override
    public Long read(JsonReader jsonReader) throws IOException {
        return Long.parseLong(jsonReader.nextString());
    }
}
