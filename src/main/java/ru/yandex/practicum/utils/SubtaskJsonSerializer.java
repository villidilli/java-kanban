package ru.yandex.practicum.utils;

import com.google.gson.*;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.ZonedDateTime;

import static ru.yandex.practicum.utils.TimeConverter.ZONED_DATE_TIME_FORMATTER;

public class SubtaskJsonSerializer implements JsonSerializer<SubTask> {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(ZonedDateTime.class, new TimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();
    @Override
    public JsonElement serialize(SubTask subTask, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", new JsonPrimitive(subTask.getType().name()));
        if (subTask.getID() == 0) {
            object.add("id", null);
        } else {
            object.add("id", new JsonPrimitive(subTask.getID()));
        }
        object.add("name", new JsonPrimitive(subTask.getName()));
        object.add("description", new JsonPrimitive(subTask.getDescription()));
        object.add("status", new JsonPrimitive(subTask.getStatus().name()));
        if (subTask.getStartTime() == Task.UNREACHEBLE_DATE) {
            object.add("startDateTime", new JsonPrimitive("--"));
        } else {
            object.add("startDateTime", new JsonPrimitive(subTask.getStartTime().format(ZONED_DATE_TIME_FORMATTER)));

        }
        object.add("duration", new JsonPrimitive(subTask.getDuration()));
        object.add("parentEpicID", new JsonPrimitive(subTask.getParentEpicID()));
        return object;
    }
}
