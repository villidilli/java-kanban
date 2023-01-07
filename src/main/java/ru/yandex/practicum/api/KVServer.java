    package ru.yandex.practicum.api;

    import com.sun.net.httpserver.HttpExchange;
    import com.sun.net.httpserver.HttpServer;
    import ru.yandex.practicum.utils.HttpConverter;

    import java.io.IOException;
    import java.net.InetSocketAddress;
    import java.util.HashMap;
    import java.util.Map;

    import static java.nio.charset.StandardCharsets.UTF_8;
    import static ru.yandex.practicum.api.HttpMethod.*;

    public class KVServer {
        public static final int PORT = 8078;
        private final String apiToken;
        private final HttpServer server;
        private final Map<String, String> data = new HashMap<>();

        public KVServer() throws IOException {
            apiToken = generateApiToken();
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/register", this::register);
            server.createContext("/save", this::save);
            server.createContext("/load", this::load);
        }

        private void load(HttpExchange exchange) {
            // TODO Добавьте получение значения по ключу
            System.out.println("\n/load");
            if (!hasAuth(exchange)) {}
        }

        private void save(HttpExchange exchange) throws IOException {
            try {
                System.out.println("\n/save");
                if (!hasAuth(exchange)) {
                    System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                    exchange.sendResponseHeaders(403, 0);
                    return;
                }
                if (POST == HttpConverter.getEnumMethod(exchange.getRequestMethod())) {
                    String token = exchange.getRequestURI().getPath().substring("/save/".length());
                    if (token.isEmpty()) {
                        System.out.println("Key для сохранения пустой. token указывается в пути: /save/{token}");
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    String body = readText(exchange);
                    if (body.isEmpty()) {
                        System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                        exchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    data.put(token, body);
                    System.out.println("Значение для ключа " + token + " успешно обновлено!");
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("/save ждёт POST-запрос, а получил: " + exchange.getRequestMethod());
                    exchange.sendResponseHeaders(405, 0);
                }
            } finally {
                exchange.close();
            }
        }

        private void register(HttpExchange exchange) throws IOException {
            try {
                System.out.println("\n/register");
                if (GET == HttpConverter.getEnumMethod(exchange.getRequestMethod())) {
                    sendText(exchange, apiToken);
                } else {
                    System.out.println("/register ждёт GET-запрос, а получил " + exchange.getRequestMethod());
                    exchange.sendResponseHeaders(405, 0);
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

        protected boolean hasAuth(HttpExchange exchange) {
            String rawQuery = exchange.getRequestURI().getRawQuery();
            return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
        }

        protected String readText(HttpExchange exchange) throws IOException {
            return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
        }

        protected void sendText(HttpExchange exchange, String text) throws IOException {
            byte[] response = text.getBytes(UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
        }
    }
