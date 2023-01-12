package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.managers.HttpTaskManager;
import ru.yandex.practicum.managers.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

import static ru.yandex.practicum.api.APIMessage.*;

public class HttpTaskServer {
    public static final int PORT = 8080;
    public static final String ROOT_PATH = "/tasks";
    private final HttpServer server;
    private APIMessage statusServer;
    private final HttpTaskManager httpManager;

    public HttpTaskServer() {
        try {
            httpManager = Managers.getDefault();
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext(ROOT_PATH, new TasksHandler(httpManager));
            statusServer = HTTP_TASK_SERVER_CREATED;
            System.out.println(HTTP_TASK_SERVER_CREATED.getMessage());
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }
    }

    public void start() {
        server.start();
        statusServer = HTTP_TASK_SERVER_STARTED;
        System.out.println(HTTP_TASK_SERVER_STARTED.getMessage());
    }

    public void stop() {
        server.stop(1);
        statusServer = HTTP_TASK_SERVER_STOPPED;
        System.out.println(HTTP_TASK_SERVER_STOPPED.getMessage());
    }

    public APIMessage getStatusServer() {
        return statusServer;
    }

    public int getGeneratedID() {
        return httpManager.getGeneratorID();
    }
}