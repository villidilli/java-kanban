package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;
    public static final String ROOT_PATH = "/tasks";
    private final HttpServer server;

    public HttpTaskServer() {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext(ROOT_PATH, new TasksHandler());
            System.out.println("[HttpTaskServer] [" + PORT + "] инициализирован");
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }
    }

    public void start() {
        server.start();
        System.out.println("[HttpTaskServer] [" + PORT + "] готов к работе");
    }
}