package ru.yandex.practicum;

import ru.yandex.practicum.api.HttpTaskServer;
import ru.yandex.practicum.api.Servers;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Servers.getHttpTaskServer().start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
