package ru.yandex.practicum.utils;

import com.google.gson.*;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import com.google.gson.JsonDeserializer;
import ru.yandex.practicum.tasks.TaskTypes;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.ZonedDateTime;

public class SubtaskJsonDeserializer implements JsonDeserializer<SubTask> {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(Status.class, new TaskStatusAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new TimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();

    @Override
    public SubTask deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonElement id = object.get("id");
        JsonElement name = object.get("name");
        JsonElement description = object.get("description");
        JsonElement status = object.get("status");
        JsonElement startDateTime = object.get("startDateTime");
        JsonElement duration = object.get("duration");
        JsonElement parentEpicID = object.get("parentEpicID");

        if (name == null || description == null || parentEpicID == null) {
            throw new JsonParseException("Необходимо передать значения полей [name/description/parentEpicID]");
        }
        SubTask subTask = new SubTask(name.getAsString(), description.getAsString(), parentEpicID.getAsInt());
        if (id != null) subTask.setID(id.getAsInt());
        if (status != null) subTask.setStatus(gson.fromJson(status, Status.class));
        if (startDateTime != null) subTask.setStartTime(gson.fromJson(startDateTime, ZonedDateTime.class));
        if (duration.getAsLong() != 0) subTask.setDuration(gson.fromJson(duration, Duration.class).toMinutes());
        return subTask;
    }
}