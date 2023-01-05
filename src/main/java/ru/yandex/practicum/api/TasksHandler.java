package ru.yandex.practicum.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ru.yandex.practicum.managers.ManagerNotFoundException;
import ru.yandex.practicum.managers.TaskManager;
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

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String PATH_PARTS_SEPARATOR = "/";
    private static final int START_INDEX_ID = 3;
    private final Gson gson;
    private final TaskManager manager;
    private HttpExchange exchange;
    private URI url;
    private HttpMethod method;

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
        System.out.println("Началась обработка запроса от клиента.");
        this.exchange = exchange;
        url = exchange.getRequestURI();
        method = HttpConverter.getEnumMethod(exchange.getRequestMethod());
        Endpoint endpoint = getEndpoint(url, method);

        switch (endpoint) {
            case GET_ALL_TASKS: handleGetAllTasks();
            case GET_ALL_SUBTASKS: handleGetAllSubtasks();
            case GET_ALL_EPICS: handleGetAllEpics();
            case GET_TASK_BY_ID: handleGetTaskByID();
            case GET_SUBTASK_BY_ID: handleGetSubtaskByID();
            case GET_EPIC_BY_ID: handleGetEpicByID();
            case GET_ALL_SUBTASKS_BY_EPIC: handleGetEpicSubtasks();
            case GET_PRIORITIZED_TASKS: handleGetPrioritizedTasks();
            case GET_HISTORY: handleGetHistory();
            case DELETE_ALL_TASKS: handleDeleteAllTasks();
            case DELETE_ALL_SUBTASKS: handleDeleteAllSubtasks();
            case DELETE_ALL_EPICS: handleDeleteAllEpics();
            case DELETE_TASK_BY_ID: handleGetTaskByID();
            case DELETE_SUBTASK_BY_ID: handleDeleteSubtaskByID();
            case DELETE_EPIC_BY_ID: handleDeleteEpicByID();
        }

    }

    private int getParameterID(URI url) {
        return Integer.parseInt(url.getQuery().substring(START_INDEX_ID));
    }

    private Endpoint getEndpoint(URI url, HttpMethod method) {
        String[] pathParts = url.getPath().split(PATH_PARTS_SEPARATOR);
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

    private void writeResponse(String responseString, int responseCode) throws IOException {
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

    private void handleGetAllTasks() throws IOException {
        writeResponse(gson.toJson(manager.getAllTasks()), 200);
    }

    private void handleGetAllSubtasks() throws IOException {
        writeResponse(gson.toJson(manager.getAllSubTasks()), 200);
    }

    private void handleGetAllEpics() throws IOException {
        writeResponse(gson.toJson(manager.getAllEpics()), 200);
    }

    private void handleGetTaskByID() throws IOException {
        int id = getParameterID(url);
        try {
            writeResponse(gson.toJson(manager.getTaskByID(id)), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpServerException.ERROR404), 404);
        }
    }

    private void handleGetSubtaskByID() throws IOException {
        int id = getParameterID(url);
        try {
            writeResponse(gson.toJson(manager.getSubTaskByID(id)), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpServerException.ERROR404), 404);
        }
    }

    private void handleGetEpicByID() throws IOException {
        int id = getParameterID(url);
        try {
            writeResponse(gson.toJson(manager.getEpicByID(id)), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpServerException.ERROR404), 404);
        }
    }

    private void handleGetEpicSubtasks() throws IOException {
        int id = getParameterID(url);
        try {
            writeResponse(gson.toJson(manager.getAllSubTasksByEpic(id)), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpServerException.ERROR404), 404);
        }
    }

    private void handleGetPrioritizedTasks() throws IOException {
        writeResponse(gson.toJson(manager.getPrioritizedTasks()), 200);
    }

    private void handleGetHistory() throws IOException {
        writeResponse(gson.toJson(manager.getHistory()), 200);
    }

    private void handleDeleteAllTasks() throws IOException {
        manager.deleteAllTasks();
        writeResponse(gson.toJson(HttpStatusCode.DELETE200.getMessage()), 200);
    }

    private void handleDeleteAllSubtasks() throws IOException {
        manager.deleteAllSubTasks();
        writeResponse(gson.toJson(HttpStatusCode.DELETE200.getMessage()), 200);
    }

    private void handleDeleteAllEpics() throws IOException {
        manager.deleteAllEpics();
        writeResponse(gson.toJson(HttpStatusCode.DELETE200.getMessage()), 200);
    }

    private void handleDeleteTaskByID() throws IOException {
        int id = getParameterID(url);
        try {
            manager.deleteTaskByID(id);
            writeResponse(gson.toJson(HttpStatusCode.DELETE200.getMessage()), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpStatusCode.ERROR404.getMessage()), 404);
        }
    }

    private void handleDeleteSubtaskByID() throws IOException {
        int id = getParameterID(url);
        try {
            manager.deleteSubTaskByID(id);
            writeResponse(gson.toJson(HttpStatusCode.DELETE200.getMessage()), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpStatusCode.ERROR404.getMessage()), 404);
        }
    }

    private void handleDeleteEpicByID() throws IOException {
        int id = getParameterID(url);
        try {
            manager.deleteEpicByID(id);
            writeResponse(gson.toJson(HttpStatusCode.DELETE200.getMessage()), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpStatusCode.ERROR404.getMessage()), 404);
        }
    }
}