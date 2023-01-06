package ru.yandex.practicum.api;

public enum HttpStatusCode {
    ERROR404 ("ERROR -> 404 Запрашиваемый объект не найден"),
    ERROR500 ("ERROR -> 500 Внутренняя ошибка сервера"),
    DELETE200 ("SUCСESS -> 200 Удаление завершено успешно"),
    ERROR400 ("ERROR -> 400 Обнаружена синтаксическая ошибка");

    private String message;
    private HttpStatusCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
