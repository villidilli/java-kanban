package ru.yandex.practicum.api;

public class HttpException extends RuntimeException {
    public static final String API_TOKEN_NOT_FOUND =
            "Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа";
    public static final String API_TOKEN_IS_EMPTY =
            "Key для сохранения пустой. token указывается в пути: /save/{token}";
    public static final String BODY_IS_EMPTY = "Ничего не передано в тело";
    public static final String METHOD_NOT_POST = "Ожидался POST запрос";
    public static final String METHOD_NOT_GET = "Ожидался GET запрос";
    public static final String NOT_PUT_TO_SERVER = "В сохранении на сервер отказано";
    public static final String NOT_LOAD_FROM_SERVER = "В загрузке с сервера отказано";

    public HttpException (String message) {
        super(message);
    }
}
