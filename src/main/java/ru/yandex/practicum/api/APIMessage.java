package ru.yandex.practicum.api;

public enum APIMessage {
    NOT_FOUND("Объект не найден"),
    DELETE_ACCEPT("Удаление завершено успешно"),
    CONTENT_TYPE("Content-Type"),
    APPLICATION_JSON("application/json"),
    NOT_INPUT_MIN_FIELD_EPIC("Необходимо установить значение полей [name/description]"),
    NOT_INPUT_MIN_FIELD_SUBTASK("Необходимо передать значения полей [name/description/parentEpicID]"),
    NOT_INPUT_MIN_FIELD_TASK("Необходимо установить значение name и description");

    private final String message;

    APIMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}