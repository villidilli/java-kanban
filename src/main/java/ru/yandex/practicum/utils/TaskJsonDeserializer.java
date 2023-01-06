package ru.yandex.practicum.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import ru.yandex.practicum.tasks.Task;
import com.google.gson.JsonDeserializer;

import java.lang.reflect.Type;

public class TaskJsonDeserializer implements JsonDeserializer<Task> {


    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
