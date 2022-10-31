package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;

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
