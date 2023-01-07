package ru.yandex.practicum.utils;

import ru.yandex.practicum.api.HttpMethod;
import ru.yandex.practicum.api.PathPart;

public class HttpConverter {
    public static HttpMethod getEnumMethod(String method) {
        switch (method.toUpperCase()) {
            case "GET": return HttpMethod.GET;
            case "POST": return HttpMethod.POST;
            case "DELETE": return HttpMethod.DELETE;
            default: return HttpMethod.UNKNOWN;
        }
    }

    public static PathPart getEnumPathPart(String pathPart) {
            switch (pathPart.toUpperCase()) {
                case "TASKS": return PathPart.TASKS;
                case "TASK": return PathPart.TASK;
                case "SUBTASK": return PathPart.SUBTASK;
                case "EPIC": return PathPart.EPIC;
                case "HISTORY": return PathPart.HISTORY;
            }
        return PathPart.NOT_REGISTERED;
    }
}
