package ru.yandex.practicum.api;

public class HttpServerException extends RuntimeException {
    public static final String UNKNOWN_REQUEST_METHOD = "ERROR -> Неизвестный метод запроса!";
    public static final String UNKNOWN_ENDPOINT = "ERROR -> Неизвестный эндпоинт";
    public HttpServerException(String message) {
        super(message);
    }
}
