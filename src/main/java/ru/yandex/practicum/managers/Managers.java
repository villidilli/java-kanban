package ru.yandex.practicum.managers;

import java.io.File;
import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

//    public static TaskManager getDefault() {
//        try {
//            return HttpTaskManager.load("http://localhost:8078");
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() { //todo заменил с filebackedmanager на taskmanager. проверить
        return FileBackedTasksManager.load(new File("src/main/resources/Backup.csv"));
    }

//    public static HttpTaskManager getDefaultHttp() {
//        try {
//            return HttpTaskManager.load("http://localhost:8078");
//        } catch (IOException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
}