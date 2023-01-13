package ru.yandex.practicum.api;

public enum APIMessage {
	NOT_FOUND("Объект не найден"),
	DELETE_ACCEPT("Удаление завершено успешно"),
	CONTENT_TYPE("Content-Type"),
	APPLICATION_JSON("application/json"),
	NOT_INPUT_MIN_FIELD_EPIC("Необходимо установить значение полей [name/description]"),
	INVALID_INPUT_FIELDS_EPIC("Ввод полей [startDateTime/duration/status] не требуется"),
	PARENT_EPIC_NOT_FOUND("Родительский эпик с указанным ID не существует"),
	NOT_INPUT_MIN_FIELD_SUBTASK("Необходимо передать значения полей [name/description/parentEpicID]"),
	NOT_INPUT_MIN_FIELD_TASK("Необходимо установить значение name и description"),
	HTTP_TASK_SERVER_CREATED("[HttpTaskServer] инициализирован"),
	HTTP_TASK_SERVER_STARTED("[HttpTaskServer] готов к работе"),
	HTTP_TASK_SERVER_STOPPED("[HttpTaskServer] остановлен"),
	ATTENTION_CODE_CONDITION("ATTENTION -> Сервер вернул код состояния: ");

	private final String message;

	APIMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}