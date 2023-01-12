package ru.yandex.practicum.api;

import java.io.IOException;

import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static java.net.http.HttpResponse.*;

import static ru.yandex.practicum.api.APIMessage.*;

public class KVTaskClient {
	private final HttpClient client;
	private final String API_TOKEN;
	private final String serverURL;
	private HttpRequest request;

	public KVTaskClient(String serverURL) throws IOException, InterruptedException, APIException {
		this.serverURL = serverURL;
		request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(serverURL + "/register"))
				.version(HttpClient.Version.HTTP_1_1)
				.headers(CONTENT_TYPE.name(), APPLICATION_JSON.name())
				.build();
		client = HttpClient.newHttpClient();
		API_TOKEN = client.send(request, BodyHandlers.ofString()).body();
		System.out.println("[KVClient] успешно зарегистрировался на [KVServer] [API_TOKEN: " + API_TOKEN + "]");
		System.out.println("[KVClient] готов к работе");
	}

	public void put(String key, String json) throws IOException, InterruptedException, APIException {
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.uri(URI.create(serverURL + "/save/" + key + "?API_TOKEN=" + API_TOKEN))
				.version(HttpClient.Version.HTTP_1_1)
				.headers(CONTENT_TYPE.name(), APPLICATION_JSON.name())
				.build();
		try {
			client.send(request, BodyHandlers.ofString());
		} catch (APIException e) {
			throw new APIException(APIException.NOT_PUT_TO_SERVER);
		}
	}

	public String load(String key) throws IOException, InterruptedException, APIException {
		request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(serverURL + "/load/" + key + "?API_TOKEN=" + API_TOKEN))
				.version(HttpClient.Version.HTTP_1_1)
				.headers(CONTENT_TYPE.name(), APPLICATION_JSON.name())
				.build();
		return client.send(request, BodyHandlers.ofString()).body();
	}
}