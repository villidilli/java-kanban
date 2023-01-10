package ru.yandex.practicum.api;

public class Servers {
    public static HttpTaskServer getHttpTaskServer() {
        return new HttpTaskServer();
    }

    public static KVServer getKVServer() {
            return new KVServer();
    }
}