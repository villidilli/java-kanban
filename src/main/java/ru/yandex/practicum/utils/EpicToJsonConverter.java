package ru.yandex.practicum.utils;

import com.google.gson.*;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.ZonedDateTime;

import static ru.yandex.practicum.utils.DateTimeConverter.ZONED_DATE_TIME_FORMATTER;

public class EpicToJsonConverter implements JsonSerializer<Epic>, JsonDeserializer<Epic> {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(Status.class, new TaskStatusAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new DateTimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();

    @Override
    public JsonElement serialize(Epic epic, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("type", new JsonPrimitive(epic.getType().name()));
        if (epic.getID() == 0) {
            object.add("id", null);
        } else {
            object.add("id", new JsonPrimitive(epic.getID()));
        }
        object.add("name", new JsonPrimitive(epic.getName()));
        object.add("description", new JsonPrimitive(epic.getDescription()));
        object.add("status", new JsonPrimitive(epic.getStatus().name()));
        if (epic.getStartTime() == Task.UNREACHEBLE_DATE) {
            object.add("startDateTime", new JsonPrimitive("--"));
        } else {
            object.add("startDateTime", new JsonPrimitive(epic.getStartTime().format(ZONED_DATE_TIME_FORMATTER)));

        }
        object.add("duration", new JsonPrimitive(epic.getDuration()));
        return object;
    }

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
