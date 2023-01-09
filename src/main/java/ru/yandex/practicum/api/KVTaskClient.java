package ru.yandex.practicum.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static java.net.http.HttpResponse.*;

public class KVTaskClient {
    private final HttpClient client;
    private HttpRequest request;
    private final String API_TOKEN;
    private final String serverURL;

    public KVTaskClient(String serverURL) throws IOException, InterruptedException, APIException {
        this.serverURL = serverURL;
        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverURL + "/register"))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Content-Type", "application/json")
                .build();
        client = HttpClient.newHttpClient();
        API_TOKEN = client.send(request, BodyHandlers.ofString()).body();
        System.out.println("[KVClient] успешно зарегистрировался на [KVServer] [API_TOKEN: " + API_TOKEN + "]");
        System.out.println("[KVClient] готов к работе");
    }

    public String getAPI_TOKEN() {
        return API_TOKEN;
    }

    public void put(String key, String json) throws IOException, InterruptedException, APIException {
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(serverURL + "/save/" + key + "?API_TOKEN=" + API_TOKEN))
                .version(HttpClient.Version.HTTP_1_1)
                .headers("Content-Type", "application/json")
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
                .headers("Content-Type", "application/json")
                .build();
//        try {
            String object = client.send(request, BodyHandlers.ofString()).body();
            return object;

//        } catch (HttpException e) {
//            throw new HttpException(HttpException.NOT_LOAD_FROM_SERVER);
//        }
    }
}
