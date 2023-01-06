package ru.yandex.practicum.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.yandex.practicum.tasks.Status;

import java.io.IOException;
import java.lang.reflect.Type;

public class TaskStatusAdapter extends TypeAdapter<Status> {

    @Override
    public void write(JsonWriter writer, Status status) throws IOException {
        if (status == Status.UNKNOWN) {
            writer.nullValue();
        }
        writer.value(status.name());
    }

    @Override
    public Status read(JsonReader reader) throws IOException {
        String status = reader.nextString();
        if (status.equals("in progress")) {
            return Status.IN_PROGRESS;
        }
        if (status.equals("done")) {
            return Status.DONE;
        }
       return Status.NEW;
    }
}


