package ru.yandex.practicum.api;

public enum HttpMessage {
    NOT_FOUND ("ОБЪЕКТ НЕ НАЙДЕН"),

    DELETE_ACCEPT("УДАЛЕНИЕ ЗАВЕРШЕНО УСПЕШНО"),
    CONTENT_TYPE("Content-Type"),
    APPLICATION_JSON("application/json");

    private final String message;
    HttpMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
