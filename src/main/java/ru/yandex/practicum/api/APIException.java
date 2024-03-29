package ru.yandex.practicum.api;

public class APIException extends RuntimeException {
	public static final String API_TOKEN_NOT_FOUND =
			"Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа";
	public static final String API_TOKEN_IS_EMPTY =
			"Key для сохранения пустой. token указывается в пути: /save/{token}";
	public static final String BODY_IS_EMPTY = "Ничего не передано в тело";
	public static final String METHOD_NOT_POST = "Ожидался POST запрос";
	public static final String METHOD_NOT_GET = "Ожидался GET запрос";
	public static final String NOT_PUT_TO_SERVER = "В сохранении на сервер отказано";

	public APIException(String message) {
		super(message);
	}
}