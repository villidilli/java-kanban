package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.utils.EpicToJsonConverter;
import ru.yandex.practicum.utils.HttpConverter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import ru.yandex.practicum.utils.SubtaskToJsonConverter;
import ru.yandex.practicum.utils.TaskToJsonConverter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.yandex.practicum.api.APIException.*;
import static ru.yandex.practicum.api.RequestMethod.*;

public class KVServer {
    public static final int PORT = 8078;
    private final String apiToken;
    private final HttpServer server;
    public final Map<String, String> data = new HashMap<>(); //todo заприватить
    private final Gson gson;

    //ok
    public KVServer() throws IOException, APIException {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskToJsonConverter())
                .registerTypeAdapter(SubTask.class, new SubtaskToJsonConverter())
                .registerTypeAdapter(Epic.class, new EpicToJsonConverter())
                .create();
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    private void load(HttpExchange exchange) throws IOException, APIException {
        try {
            isHaveApiTokenInQuery(exchange);
            String dataObject = data.get(getKeyFromRequest(exchange));
            if (dataObject != null) {
                sendResponse(exchange, dataObject, 200);
                return;
            }
            sendResponse(exchange, 200);
        } finally {
            exchange.close();
        }
    }

    //ok
    private void save(HttpExchange exchange) throws IOException, APIException {
        try {
            isHaveApiTokenInQuery(exchange);
            if (POST == HttpConverter.getEnumMethod(exchange.getRequestMethod())) {
                String key = getKeyFromRequest(exchange);
                String requestBody = getRequestBody(exchange);
                data.put(key, requestBody);
                sendResponse(exchange, requestBody, 200);
            } else {
                sendResponse(exchange, gson.toJson(METHOD_NOT_POST), 405);
                throw new APIException(METHOD_NOT_POST);
            }
        } finally {
            exchange.close();
        }
    }

    private void register(HttpExchange exchange) throws IOException, APIException {
        try {
            if (GET == HttpConverter.getEnumMethod(exchange.getRequestMethod())) {
                sendResponse(exchange, apiToken, 200);
            } else {
                sendResponse(exchange, gson.toJson(METHOD_NOT_GET), 405);
                throw new APIException(METHOD_NOT_GET);
            }
        } finally {
            exchange.close();
        }
    }

    public void start() {
        System.out.println("[" + this.getClass().getSimpleName() + "] запущен на порту [" + PORT + "]");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    //ok
    private boolean hasAuth(HttpExchange exchange) {
        String rawQuery = exchange.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    //ok
    private void isHaveApiTokenInQuery(HttpExchange exchange) throws IOException, APIException {
        if (!hasAuth(exchange)) {
            sendResponse(exchange, gson.toJson(API_TOKEN_NOT_FOUND), 403);
            throw new APIException(API_TOKEN_NOT_FOUND);
        }
    }

    //ok
    private String getKeyFromRequest(HttpExchange exchange) throws IOException, APIException {
        String key = exchange.getRequestURI().getPath().substring("/save/".length());
        if (key.isEmpty()) {
            sendResponse(exchange, gson.toJson(API_TOKEN_IS_EMPTY), 400);
            throw new APIException(API_TOKEN_IS_EMPTY);
        }
        return key;
    }

    //ok
    private String getRequestBody(HttpExchange exchange) throws IOException, APIException {
            String body = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
            if (body.isEmpty()) {
                sendResponse(exchange, gson.toJson(BODY_IS_EMPTY), 400);
                throw new APIException(BODY_IS_EMPTY);
            }
            return body;
    }

    //ok
    private void sendResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
            return;
        }
        byte[] response = responseString.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(responseCode, response.length);
        exchange.getResponseBody().write(response);
    }

    private void sendResponse(HttpExchange exchange, int responseCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(responseCode, 0);
    }
}