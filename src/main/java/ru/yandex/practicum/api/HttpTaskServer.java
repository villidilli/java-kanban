package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.managers.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String ROOT_PATH = "/tasks";
    private final HttpServer server;
    private final FileBackedTasksManager backedManager;

    public HttpTaskServer() {
        try {
            backedManager = new FileBackedTasksManager();
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext(ROOT_PATH, new TasksHandler(backedManager));
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }
    }

    public void start() {
        server.start();
        System.out.println("[" + this.getClass().getSimpleName() + "] запущен на порту [" + PORT + "]");

    }


}
