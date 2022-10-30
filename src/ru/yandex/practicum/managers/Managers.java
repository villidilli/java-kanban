package ru.yandex.practicum.managers;

public class Managers {

    private Managers(){};

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
}
