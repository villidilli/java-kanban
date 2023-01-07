//package ru.yandex.practicum.utils;
//
//import com.google.gson.*;
//import ru.yandex.practicum.tasks.Status;
//import ru.yandex.practicum.tasks.Task;
//import com.google.gson.JsonDeserializer;
//import ru.yandex.practicum.tasks.TaskTypes;
//
//import java.lang.reflect.Type;
//import java.time.Duration;
//import java.time.ZonedDateTime;
//
//public class TaskJsonDeserializer implements JsonDeserializer<Task> {
////    Gson gson = new GsonBuilder()
////            .setPrettyPrinting()
////            .serializeNulls()
////            .registerTypeAdapter(Status.class, new TaskStatusAdapter())
////            .registerTypeAdapter(ZonedDateTime.class, new TimeConverter())
////            .registerTypeAdapter(Duration.class, new DurationConverter())
////            .create();
////
////    @Override
////    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
////            JsonObject object = json.getAsJsonObject();
////            JsonElement id = object.get("id");
////            JsonElement name = object.get("name");
////            JsonElement description = object.get("description");
////            JsonElement status = object.get("status");
////            JsonElement startDateTime = object.get("startDateTime");
////            JsonElement duration = object.get("duration");
////
////            if (name == null || description == null) {
////                throw new JsonParseException("Необходимо установить значение name и description");
////            }
////            Task task = new Task(name.getAsString(), description.getAsString());
////            if (id != null) task.setID(id.getAsInt());
////            if (status != null) task.setStatus(gson.fromJson(status, Status.class));
////            if (startDateTime != null) task.setStartTime(gson.fromJson(startDateTime, ZonedDateTime.class));
////            if (duration.getAsLong() != 0) task.setDuration(gson.fromJson(duration, Duration.class).toMinutes());
////            return task;
//    }
//
//
//
//}
