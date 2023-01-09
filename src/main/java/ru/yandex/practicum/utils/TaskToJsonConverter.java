package ru.yandex.practicum.utils;

import com.google.gson.*;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.ZonedDateTime;

import static ru.yandex.practicum.utils.DateTimeConverter.ZONED_DATE_TIME_FORMATTER;

public class TaskToJsonConverter implements JsonSerializer<Task>, JsonDeserializer<Task> {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(Status.class, new TaskStatusAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new DateTimeConverter())
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

    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonElement id = object.get("id");
        JsonElement name = object.get("name");
        JsonElement description = object.get("description");
        JsonElement status = object.get("status");
        JsonElement startDateTime = object.get("startDateTime");
        JsonElement duration = object.get("duration");

        if (name == null || description == null) {
            throw new JsonParseException("Необходимо установить значение name и description");
        }
        Task task = new Task(name.getAsString(), description.getAsString());
        if (id != null) task.setID(id.getAsInt());
        if (status != null) task.setStatus(gson.fromJson(status, Status.class));
        if (startDateTime != null) task.setStartTime(gson.fromJson(startDateTime, ZonedDateTime.class));
        if (duration != null && duration.getAsLong() != 0) task.setDuration(gson.fromJson(duration, Duration.class).toMinutes());
        return task;
    }
}
