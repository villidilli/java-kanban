package ru.yandex.practicum.utils;

import com.google.gson.*;

import ru.yandex.practicum.tasks.*;

import java.lang.reflect.Type;

import java.time.Duration;
import java.time.ZonedDateTime;

import static ru.yandex.practicum.api.APIMessage.NOT_INPUT_MIN_FIELD_SUBTASK;
import static ru.yandex.practicum.utils.DateTimeConverter.ZONED_DATE_TIME_FORMATTER;

public class SubtaskToJsonConverter implements JsonSerializer<SubTask>, JsonDeserializer<SubTask> {
    private final Gson gson = GsonConfig.getGsonConverterConfig();

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
            object.add("startDateTime", new JsonPrimitive(
                    subTask.getStartTime().format(ZONED_DATE_TIME_FORMATTER)
            ));
        }
        object.add("duration", new JsonPrimitive(subTask.getDuration()));
        object.add("parentEpicID", new JsonPrimitive(subTask.getParentEpicID()));
        return object;
    }

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
            throw new JsonParseException(gson.toJson(NOT_INPUT_MIN_FIELD_SUBTASK));
        }
        SubTask subTask = new SubTask(name.getAsString(), description.getAsString(), parentEpicID.getAsInt());
        if (id != null) subTask.setID(id.getAsInt());
        if (status != null) subTask.setStatus(gson.fromJson(status, Status.class));
        if (startDateTime != null) subTask.setStartTime(gson.fromJson(startDateTime, ZonedDateTime.class));
        if (duration != null && duration.getAsLong() != 0) {
            subTask.setDuration(gson.fromJson(duration, Duration.class).toMinutes());
        }
        return subTask;
    }
}