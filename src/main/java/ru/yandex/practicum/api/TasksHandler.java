package ru.yandex.practicum.api;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.managers.ManagerNotFoundException;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.utils.*;


import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;

import static ru.yandex.practicum.api.Endpoint.*;
import static ru.yandex.practicum.api.HttpStatusCode.REQUEST_BODY_NULL;

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String PATH_PARTS_SEPARATOR = "/";
    private static final int START_INDEX_ID = 3;
    private final Gson gson;
    private final TaskManager manager;
    private HttpExchange exchange;
    private URI url;
    private HttpMethod method;
    private JsonElement body;



    public TasksHandler(TaskManager backedManager) {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
//                .registerTypeAdapter(ZonedDateTime.class, new TimeConverter())
//                .registerTypeAdapter(Duration.class, new DurationConverter())
                .registerTypeAdapter(Task.class, new TaskJsonSerilizer())
                .registerTypeAdapter(Task.class, new TaskJsonDeserializer())
                .registerTypeAdapter(SubTask.class, new SubtaskJsonDeserializer())
                .registerTypeAdapter(Epic.class, new EpicJsonDeserializer())
                .create();
        manager = backedManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка запроса от клиента.");
        this.exchange = exchange;
        url = exchange.getRequestURI();
        method = HttpConverter.getEnumMethod(exchange.getRequestMethod());
        body = JsonParser.parseString(new String(exchange.getRequestBody().readAllBytes()));

        Endpoint endpoint = getEndpoint(url, method);

        switch (endpoint) {
            case GET_ALL_TASKS:
                handleGetAllTasks();
                break;
            case GET_ALL_SUBTASKS:
                handleGetAllSubtasks();
                break;
            case GET_ALL_EPICS:
                handleGetAllEpics();
                break;
            case GET_TASK_BY_ID:
                handleGetTaskByID();
                break;
            case GET_SUBTASK_BY_ID:
                handleGetSubtaskByID();
                break;
            case GET_EPIC_BY_ID:
                handleGetEpicByID();
                break;
            case GET_ALL_SUBTASKS_BY_EPIC:
                handleGetEpicSubtasks();
                break;
            case GET_PRIORITIZED_TASKS:
                handleGetPrioritizedTasks();
                break;
            case GET_HISTORY:
                handleGetHistory();
                break;
            case DELETE_ALL_TASKS:
                handleDeleteAllTasks();
                break;
            case DELETE_ALL_SUBTASKS:
                handleDeleteAllSubtasks();
                break;
            case DELETE_ALL_EPICS:
                handleDeleteAllEpics();
                break;
            case DELETE_TASK_BY_ID:
                handleDeleteTaskByID();
                break;
            case DELETE_SUBTASK_BY_ID:
                handleDeleteSubtaskByID();
                break;
            case DELETE_EPIC_BY_ID:
                handleDeleteEpicByID();
                break;
            case CREATE_TASK: handleCreateTask(); break;
            case UPDATE_TASK: handleUpdateTask(); break;


        }

    }

    private int getParameterID(URI url) {
        return Integer.parseInt(url.getQuery().substring(START_INDEX_ID));
    }

    private Endpoint getEndpoint(URI url, HttpMethod method) throws IOException {
        String[] pathParts = url.getPath().split(PATH_PARTS_SEPARATOR);
        boolean isHaveID = url.getQuery() != null;

        if (pathParts.length == 2) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        }
        PathPart pathPart = HttpConverter.getEnumPathPart(pathParts[2]);
        if (pathParts.length == 3) {
            switch (pathPart) {
                case TASK:
                    switch (method) {
                        case GET:
                            return isHaveID ? GET_TASK_BY_ID : GET_ALL_TASKS;
                        case DELETE:
                            return isHaveID ? DELETE_TASK_BY_ID : DELETE_ALL_TASKS;
                        case POST:
                            return isHaveIdInBody() ? UPDATE_TASK : CREATE_TASK;
                    }
                case SUBTASK:
                    switch (method) {
                        case GET:
                            return isHaveID ? GET_SUBTASK_BY_ID : GET_ALL_SUBTASKS;
                        case DELETE:
                            return isHaveID ? DELETE_SUBTASK_BY_ID : DELETE_ALL_SUBTASKS;
                        case POST:
                            return isHaveIdInBody() ? UPDATE_SUBTASK : CREATE_SUBTASK;
                    }
                case EPIC:
                    switch (method) {
                        case GET:
                            return isHaveID ? GET_EPIC_BY_ID : GET_ALL_EPICS;
                        case DELETE:
                            return isHaveID ? DELETE_EPIC_BY_ID : DELETE_ALL_EPICS;
                        case POST:
                            return isHaveIdInBody() ? UPDATE_EPIC : CREATE_EPIC;
                    }
                case HISTORY:
                    return GET_HISTORY;
            }
        }
        if (pathParts.length == 4 && isHaveID) {
            return GET_ALL_SUBTASKS_BY_EPIC;
        }
        return UNKNOWN;
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
        writeResponse(gson.toJson(HttpStatusCode.DELETE_ACCEPT.getMessage()), 200);
    }

    private void handleDeleteAllSubtasks() throws IOException {
        manager.deleteAllSubTasks();
        writeResponse(gson.toJson(HttpStatusCode.DELETE_ACCEPT.getMessage()), 200);
    }

    private void handleDeleteAllEpics() throws IOException {
        manager.deleteAllEpics();
        writeResponse(gson.toJson(HttpStatusCode.DELETE_ACCEPT.getMessage()), 200);
    }

    private void handleDeleteTaskByID() throws IOException {
        int id = getParameterID(url);
        try {
            manager.deleteTaskByID(id);
            writeResponse(gson.toJson(HttpStatusCode.DELETE_ACCEPT.getMessage()), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpStatusCode.NOT_FOUND.getMessage()), 404);
        }
    }

    private void handleDeleteSubtaskByID() throws IOException {
        int id = getParameterID(url);
        try {
            manager.deleteSubTaskByID(id);
            writeResponse(gson.toJson(HttpStatusCode.DELETE_ACCEPT.getMessage()), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpStatusCode.NOT_FOUND.getMessage()), 404);
        }
    }

    private void handleDeleteEpicByID() throws IOException {
        int id = getParameterID(url);
        try {
            manager.deleteEpicByID(id);
            writeResponse(gson.toJson(HttpStatusCode.DELETE_ACCEPT.getMessage()), 200);
        } catch (ManagerNotFoundException e) {
            writeResponse(gson.toJson(HttpStatusCode.NOT_FOUND.getMessage()), 404);
        }
    }

    private void handleCreateTask() throws IOException{
        System.out.println("Запущен обработчик handleCreateTask");
        System.out.println(body);
        Task task = gson.fromJson(body, Task.class);
        manager.create(task);
        System.out.println(manager.getTaskByID(task.getID()));
        writeResponse(gson.toJson(task), 200);
    }

    private void handleUpdateTask() throws IOException {
        System.out.println("Запущен обработчик handleUpdateTask");
        System.out.println("шаг второй");
    }

    private boolean isHaveIdInBody() throws IOException {
        return body != null && body.isJsonObject() && body.getAsJsonObject().has("id");
    }

}