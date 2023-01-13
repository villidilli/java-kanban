package ru.yandex.practicum.api;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.*;

import static ru.yandex.practicum.api.APIMessage.*;

public class KVTaskClient {
	private final HttpClient client;
	private String API_TOKEN;
	private final int serverPort;
	private HttpRequest request;
	private HttpResponse<String > response;

	public KVTaskClient(int serverPort) throws IOException, InterruptedException, APIException {
		this.serverPort = serverPort;
		client = HttpClient.newHttpClient();
		register();
	}

	private URI collectURI(String path) {
		return URI.create("http://localhost:" + serverPort + path);
	}

	private void checkStatusCode(HttpResponse<String> response) {
		if (response.statusCode() != 200) {
			System.out.println(ATTENTION_CODE_CONDITION.getMessage() + response.statusCode());
			System.out.println(response.body());
		}
	}

	private void register() {
		try {
			request = HttpRequest.newBuilder()
					.GET()
					.uri(collectURI("/register"))
					.version(HttpClient.Version.HTTP_1_1)
					.headers(CONTENT_TYPE.name(), APPLICATION_JSON.name())
					.build();
			response = client.send(request, BodyHandlers.ofString());
			checkStatusCode(response);
			API_TOKEN = response.body();
			System.out.println("[KVClient] успешно зарегистрировался на [KVServer] [API_TOKEN: " + API_TOKEN + "]");
			System.out.println("[KVClient] готов к работе");
		} catch (IOException | InterruptedException e) {
			throw new APIException(e.getMessage());
		}
	}

	public void put(String key, String json) throws IOException, InterruptedException, APIException {
		try {
			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(json))
					.uri(collectURI("/save/" + key + "?API_TOKEN=" + API_TOKEN))
					.version(HttpClient.Version.HTTP_1_1)
					.headers(CONTENT_TYPE.name(), APPLICATION_JSON.name())
					.build();
			response = client.send(request, BodyHandlers.ofString());
			checkStatusCode(response);
		} catch (APIException e) {
			throw new APIException(APIException.NOT_PUT_TO_SERVER);
		}
	}

	public String load(String key) throws IOException, InterruptedException, APIException {
		request = HttpRequest.newBuilder()
				.GET()
				.uri(collectURI("/load/" + key + "?API_TOKEN=" + API_TOKEN))
				.version(HttpClient.Version.HTTP_1_1)
				.headers(CONTENT_TYPE.name(), APPLICATION_JSON.name())
				.build();
		response = client.send(request, BodyHandlers.ofString());
		checkStatusCode(response);
		return response.body();
	}
}