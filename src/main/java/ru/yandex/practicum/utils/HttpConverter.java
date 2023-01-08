package ru.yandex.practicum.utils;

import ru.yandex.practicum.api.RequestMethod;
import ru.yandex.practicum.api.PathPart;

public class HttpConverter {
    public static RequestMethod getEnumMethod(String method) {
        switch (method.toUpperCase()) {
            case "GET": return RequestMethod.GET;
            case "POST": return RequestMethod.POST;
            case "DELETE": return RequestMethod.DELETE;
            default: return RequestMethod.UNKNOWN;
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
