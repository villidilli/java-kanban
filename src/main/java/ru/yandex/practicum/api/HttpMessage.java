package ru.yandex.practicum.api;

public enum HttpMessage {
    NOT_FOUND ("ОБЪЕКТ НЕ НАЙДЕН"),
    ERROR500 ("ERROR -> 500 Внутренняя ошибка сервера"),
    DELETE_ACCEPT("УДАЛЕНИЕ ЗАВЕРШЕНО УСПЕШНО"),
    ERROR400 ("ERROR -> 400 Обнаружена синтаксическая ошибка"),
    REQUEST_BODY_NULL("ОТСУТСТВУЕТ ТЕЛО ЗАПРОСА");

    private String message;
    private HttpMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
