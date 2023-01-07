package ru.yandex.practicum.api;

import java.io.IOException;

public class Servers {
    public static HttpTaskServer getHttpTaskServer() throws IOException {
        return new HttpTaskServer();
    }

    public static KVServer getKVServer() throws IOException {
        return new KVServer();
    }
}
