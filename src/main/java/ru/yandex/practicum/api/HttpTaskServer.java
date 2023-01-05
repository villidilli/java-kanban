package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.managers.FileBackedTasksManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String ROOT_PATH = "/tasks";
    private final HttpServer server;
    private final FileBackedTasksManager backedManager;

    public HttpTaskServer() throws IOException {
        backedManager = Managers.getDefaultFileBacked();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext(ROOT_PATH, new TasksHandler(backedManager));
    }

    public void start() {
        server.start();
        System.out.println("Сервер запущен на " + PORT + " порту!");
    }


}
