package ru.yandex.practicum.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.yandex.practicum.tasks.TaskTypes;

import java.io.IOException;

public class TaskTypeAdapter extends TypeAdapter<TaskTypes> {
    @Override
    public void write(JsonWriter out, TaskTypes value) throws IOException {

    }

    @Override
    public TaskTypes read(JsonReader in) throws IOException {
        String type = in.nextString();
        if (type.equals("task")) {
            return TaskTypes.TASK;
        }
        if (type.equals("subtask")) {
            return TaskTypes.SUBTASK;
        }
        return TaskTypes.EPIC;
    }
}
