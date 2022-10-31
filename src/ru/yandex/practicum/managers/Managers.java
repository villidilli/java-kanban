package ru.yandex.practicum.managers;

public class Managers {

    private static final TaskManager defaultTaskManager = new InMemoryTaskManager();
    private static final HistoryManager defaultHistoryManager = new InMemoryHistoryManager();

    private Managers(){};

    public static TaskManager getDefault(){
        return defaultTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return defaultHistoryManager;
    }
}