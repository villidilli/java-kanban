package ru.yandex.practicum.api;

import ru.yandex.practicum.managers.HttpTaskManager;

import java.io.IOException;

public class Servers {
    public static HttpTaskServer getHttpTaskServer() {
        return new HttpTaskServer();
    }

    public static KVServer getKVServer() {
            return new KVServer();
    }
}
