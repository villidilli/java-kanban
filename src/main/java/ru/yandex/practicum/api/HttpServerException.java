package ru.yandex.practicum.api;

public class HttpServerException extends RuntimeException {
    public static final String UNKNOWN_REQUEST_METHOD = "ERROR -> Неизвестный метод запроса!";
    public static final String UNKNOWN_ENDPOINT = "ERROR -> Неизвестный эндпоинт";
    public static final String UNKNOWN_PATHPART = "ERROR -> Неизвестная часть пути";
    public static final String NOT_WRITE_RESPONCE = "ERROR -> Невозможно записать ответ";
    public static final String BIND_PROBLEM = "ERROR -> Невозможно привязаться к адресу, либо сервер уже привязан";
    public static final String IO_PROBLEM = "ERROR -> Проблема IO";
    public static final String ERROR404 = "ERROR -> 404 Запрашиваемый объект не найден";
    public static final String ERROR500 = "ERROR -> 500 Внутренняя ошибка сервера";
    public HttpServerException(String message) {
        super(message);
    }
}
