package ru.yandex.practicum.utils;

import com.google.gson.*;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.ZonedDateTime;

import static ru.yandex.practicum.utils.TimeConverter.ZONED_DATE_TIME_FORMATTER;

public class TaskJsonSerilizer implements JsonSerializer<Task> {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(ZonedDateTime.class, new TimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();
    @Override
    public JsonElement serialize(Task task, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", new JsonPrimitive(task.getType().name()));
        if (task.getID() == 0) {
            object.add("id", null);
        } else {
            object.add("id", new JsonPrimitive(task.getID()));
        }
        object.add("name", new JsonPrimitive(task.getName()));
        object.add("description", new JsonPrimitive(task.getDescription()));
        object.add("status", new JsonPrimitive(task.getStatus().name()));
        object.add("status", new JsonPrimitive(task.getStatus().name()));
        if (task.getStartTime() == Task.UNREACHEBLE_DATE) {
            object.add("startDateTime", new JsonPrimitive("--"));
        } else {
            object.add("startDateTime", new JsonPrimitive(task.getStartTime().format(ZONED_DATE_TIME_FORMATTER)));

        }
        object.add("duration", new JsonPrimitive(task.getDuration()));
        return object;
    }
}
