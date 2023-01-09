package ru.yandex.practicum.managers;

import ru.yandex.practicum.api.KVServer;

import java.io.File;
import java.io.IOException;

public class Managers {

//    public static TaskManager getDefault() {
//        return new InMemoryTaskManager();
//    }

    public static TaskManager getDefault() {
        try {
            KVServer server = new KVServer();
            server.start();
            return new HttpTaskManager("http://localhost:8078");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() { //todo заменил с filebackedmanager на taskmanager. проверить
        return FileBackedTasksManager.load(new File("src/main/resources/Backup.csv"));
    }

    public static TaskManager getDefaultHttp() {
        return new HttpTaskManager("http://localhost:8078");
    }
}