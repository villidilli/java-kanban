package ru.yandex.practicum.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.utils.DurationConverter;
import ru.yandex.practicum.utils.HttpConverter;
import ru.yandex.practicum.utils.TimeConverter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final String PATH_PARTS_SEPARATOR = "/";
    public static final int START_INDEX_ID = 3;
    Gson gson;
    TaskManager manager;

    public TasksHandler(TaskManager backedManager) {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(ZonedDateTime.class, new TimeConverter())
                .registerTypeAdapter(Duration.class, new DurationConverter())
                .create();
        manager = backedManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /hello запроса от клиента.");
        Endpoint endpoint = getEndpoint(exchange);
        switch (endpoint) {
            case GET_ALL_TASKS: handleGetAllTasks(exchange);
            case GET_ALL_SUBTASKS: handleGetAllSubtasks(exchange);

        }

    }

    private int getParameterID(URI url) {
        return Integer.parseInt(url.getQuery().substring(START_INDEX_ID));
    }

    private Endpoint getEndpoint(HttpExchange exchange) {
        URI url = exchange.getRequestURI();
        String[] pathParts = url.getPath().split(PATH_PARTS_SEPARATOR);
        HttpMethod method = HttpConverter.getEnumMethod(exchange.getRequestMethod());
        boolean isNotHaveParameters = url.getQuery() == null;

        if (pathParts.length == 2) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        }
        PathPart pathPart = HttpConverter.getEnumPathPart(pathParts[2]);
        if (pathParts.length == 3) {
            switch (pathPart) {
                case TASK:
                    switch (method) {
                        case GET:
                            return isNotHaveParameters ? Endpoint.GET_ALL_TASKS : Endpoint.GET_TASK_BY_ID;
                        case DELETE:
                            return isNotHaveParameters ? Endpoint.DELETE_ALL_TASKS : Endpoint.DELETE_TASK_BY_ID;
                        case POST:
                            return Endpoint.POST_TASK;
                    }
                case SUBTASK:
                    switch (method) {
                        case GET:
                            return isNotHaveParameters ? Endpoint.GET_ALL_SUBTASKS : Endpoint.GET_SUBTASK_BY_ID;
                        case DELETE:
                            return isNotHaveParameters ? Endpoint.DELETE_ALL_SUBTASKS : Endpoint.DELETE_SUBTASK_BY_ID;
                        case POST:
                            return Endpoint.POST_SUBTASK;
                    }
                case EPIC:
                    switch (method) {
                        case GET:
                            return isNotHaveParameters ? Endpoint.GET_ALL_EPICS : Endpoint.GET_EPIC_BY_ID;
                        case DELETE:
                            return isNotHaveParameters ? Endpoint.DELETE_ALL_EPICS : Endpoint.DELETE_EPIC_BY_ID;
                        case POST:
                            return Endpoint.POST_EPIC;
                    }
                case HISTORY:
                    return Endpoint.GET_HISTORY;
            }
        }
        if (pathParts.length == 4 && !isNotHaveParameters) {
            return Endpoint.GET_ALL_SUBTASKS_BY_EPIC;
        }
        return Endpoint.UNKNOWN;
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException{
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
                return;
            }
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            exchange.close();
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        writeResponse(exchange,gson.toJson(manager.getAllTasks()), 200);
    }

    private void handleGetAllSubtasks(HttpExchange exchange) throws IOException {

        writeResponse(exchange,gson.toJson(manager.getAllEpics()), 200);

    }

}