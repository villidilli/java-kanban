package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.utils.Endpoint;
import ru.yandex.practicum.utils.Methods;

import java.io.IOException;
import java.net.URI;

public class TasksHandler implements HttpHandler {
    public static final String PATH_PARTS_SEPARATOR = "/";
    public static final String PARAMETERS_SEPARATOR = "&";
    public static final int START_INDEX_ID = 3;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI url = exchange.getRequestURI();

    }

    private Endpoint getEndpoint(HttpExchange exchange, URI url) {
        String[] pathParts = url.getPath().split(PATH_PARTS_SEPARATOR);
        Methods method = getEnumMethod(exchange.getRequestMethod());
        boolean isNotHaveParameters = url.getQuery() == null;

        if (!pathParts[1].equals("tasks")) {
            throw new HttpServerException(HttpServerException.UNKNOWN_ENDPOINT);
        }
        if (method == Methods.GET) {
                if (pathParts.length == 2) {
                    return Endpoint.GET_PRIORITIZED_TASKS;
                }
                if (pathParts.length == 3) {
                    switch (pathParts[2]){
                        case "task":
                            return isNotHaveParameters ? Endpoint.GET_ALL_TASKS : Endpoint.GET_TASK_BY_ID; break;
                        case "subtask":
                            return isNotHaveParameters ? Endpoint.GET_ALL_SUBTASKS : Endpoint.GET_SUBTASK_BY_ID; break;
                        case "epic":
                            return isNotHaveParameters ? Endpoint.GET_ALL_EPICS : Endpoint.GET_EPIC_BY_ID; break;
                        case "history":
                            return Endpoint.GET_HISTORY; break;
                        default:
                            throw new HttpServerException(HttpServerException.UNKNOWN_ENDPOINT);
                    }
                }
            if (pathParts.length == 4 && !isNotHaveParameters) {
                return Endpoint.GET_ALL_SUBTASKS_BY_EPIC;
            }
            throw new HttpServerException(HttpServerException.UNKNOWN_ENDPOINT);
        }
        if (method == Methods.DELETE) {
            if (pathParts.length == 3) {
                switch (pathParts[2]){
                    case "task":
                       return isNotHaveParameters ? Endpoint.DELETE_ALL_TASKS : Endpoint.DELETE_TASK_BY_ID; break;
                    case "subtask":
                       return isNotHaveParameters ? Endpoint.DELETE_ALL_SUBTASKS : Endpoint.DELETE_SUBTASK_BY_ID; break;
                    case "epic":
                       return isNotHaveParameters ? Endpoint.DELETE_ALL_EPICS : Endpoint.DELETE_EPIC_BY_ID; break;
                    default:
                       throw new HttpServerException(HttpServerException.UNKNOWN_ENDPOINT);
                }
            }
            throw new HttpServerException(HttpServerException.UNKNOWN_ENDPOINT);
        }
        //todo Описать POST
        throw new HttpServerException(HttpServerException.UNKNOWN_REQUEST_METHOD);
    }

    private Methods getEnumMethod(String method) {
        switch (method) {
            case "GET": return Methods.GET;
            case "POST": return Methods.POST;
            case "DELETE": return Methods.DELETE;
            default: throw new HttpServerException(HttpServerException.UNKNOWN_REQUEST_METHOD);
        }
    }

    private int getParameterID(URI url) {
        return Integer.parseInt(url.getQuery().substring(START_INDEX_ID));
    }
}
