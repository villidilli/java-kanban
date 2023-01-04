package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static TaskManager manager;

    public static void create() throws IOException {
        manager = Managers.getDefaultFileBacked();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new)
    }

    private static void createHandler(String path) throws IOException {
        class Handler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

            }
        }

    }


}
