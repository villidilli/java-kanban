package ru.yandex.practicum.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import ru.yandex.practicum.utils.HttpConverter;

import java.io.IOException;

import java.net.InetSocketAddress;

import java.util.*;

import com.google.gson.*;

import ru.yandex.practicum.utils.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.yandex.practicum.api.APIException.*;
import static ru.yandex.practicum.api.APIMessage.*;
import static ru.yandex.practicum.api.RequestMethod.*;

public class KVServer {
	public static final int PORT = 8078;
	private final String apiToken;
	private final HttpServer server;
	private final Map<String, String> data = new HashMap<>();
	private final Gson gson;

	public KVServer() throws APIException {
		gson = GsonConfig.getGsonTaskConfig();
		apiToken = generateApiToken();
		try {
			server = HttpServer.create(new InetSocketAddress(PORT), 0);
		} catch (IOException e) {
			throw new APIException(e.getMessage());
		}
		server.createContext("/register", this::register);
		server.createContext("/save", this::save);
		server.createContext("/load", this::load);
		System.out.println("[KVServer] [" + PORT + "] инициализирован");
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

	private String generateApiToken() {
		return "" + System.currentTimeMillis();
	}

	private boolean hasAuth(HttpExchange exchange) {
		String rawQuery = exchange.getRequestURI().getRawQuery();
		return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
	}

	private void isHaveApiTokenInQuery(HttpExchange exchange) throws IOException, APIException {
		if (!hasAuth(exchange)) {
			sendResponse(exchange, gson.toJson(API_TOKEN_NOT_FOUND), 403);
			throw new APIException(API_TOKEN_NOT_FOUND);
		}
	}

	private String getKeyFromRequest(HttpExchange exchange) throws IOException, APIException {
		String key = exchange.getRequestURI().getPath().substring("/save/".length());
		if (key.isEmpty()) {
			sendResponse(exchange, gson.toJson(API_TOKEN_IS_EMPTY), 400);
			throw new APIException(API_TOKEN_IS_EMPTY);
		}
		return key;
	}

	private String getRequestBody(HttpExchange exchange) throws IOException, APIException {
		String body = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
		if (body.isEmpty()) {
			sendResponse(exchange, gson.toJson(BODY_IS_EMPTY), 400);
			throw new APIException(BODY_IS_EMPTY);
		}
		return body;
	}

	private void sendResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
		if (responseString.isBlank()) {
			exchange.sendResponseHeaders(responseCode, 0);
			return;
		}
		byte[] response = responseString.getBytes(UTF_8);
		exchange.getResponseHeaders().add(CONTENT_TYPE.name(), APPLICATION_JSON.name());
		exchange.sendResponseHeaders(responseCode, response.length);
		exchange.getResponseBody().write(response);
	}

	private void sendResponse(HttpExchange exchange, int responseCode) throws IOException {
		exchange.getResponseHeaders().add(CONTENT_TYPE.name(), APPLICATION_JSON.name());
		exchange.sendResponseHeaders(responseCode, 0);
	}

	public void start() {
		System.out.println("[KVServer] [" + PORT + "] [API_TOKEN: " + apiToken + "] готов к работе");
		server.start();
	}

	public void stop() {
		System.out.println("[[KVServer][" + PORT + "] остановлен ");
		server.stop(0);
	}
}