package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class Servers {
    public static HttpTaskServer getHttpTaskServer() throws IOException {
        return new HttpTaskServer();
    }
}
