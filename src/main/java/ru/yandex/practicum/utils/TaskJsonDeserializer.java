package ru.yandex.practicum.utils;

import com.google.gson.*;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Task;
import com.google.gson.JsonDeserializer;
import ru.yandex.practicum.tasks.TaskTypes;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.ZonedDateTime;

public class TaskJsonDeserializer implements JsonDeserializer<Task> {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(Status.class, new TaskStatusAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new TimeConverter())
            .registerTypeAdapter(Long.class, new DurationConverter())
            .create();

    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            int ID = object.get("id").getAsInt();
            String name = object.get("name").getAsString();
            String description = object.get("description").getAsString();
            Status status = gson.fromJson(object.get("status"), Status.class);
            ZonedDateTime startDateTime = gson.fromJson(object.get("startDateTime"), ZonedDateTime.class);
            Long duration = gson.fromJson(object.get("duration"), Long.class);


        return new Task(ID, name, description, status, startDateTime, duration );
    }
}
