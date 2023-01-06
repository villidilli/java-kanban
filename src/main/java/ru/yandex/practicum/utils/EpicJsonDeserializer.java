package ru.yandex.practicum.utils;

import com.google.gson.*;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Task;
import com.google.gson.JsonDeserializer;
import ru.yandex.practicum.tasks.TaskTypes;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.ZonedDateTime;

public class EpicJsonDeserializer implements JsonDeserializer<Epic> {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(Status.class, new TaskStatusAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new TimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();

    @Override
    public Epic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonElement id = object.get("id");
        JsonElement name = object.get("name");
        JsonElement description = object.get("description");


        if (name == null || description == null) {
            throw new JsonParseException("Необходимо установить значение полей [name/description]");
        }
        Epic epic = new Epic(name.getAsString(), description.getAsString());
        if (id != null) epic.setID(id.getAsInt());
        return epic;
    }
}
