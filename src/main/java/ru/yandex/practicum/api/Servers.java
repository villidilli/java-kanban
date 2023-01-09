package ru.yandex.practicum.api;

import ru.yandex.practicum.managers.HttpTaskManager;

public class Servers {
    public static HttpTaskServer createHttpTaskServer() {
        return new HttpTaskServer();
    }
}
