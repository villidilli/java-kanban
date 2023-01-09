package ru.yandex.practicum.tasks;

import java.time.ZonedDateTime;
import java.util.HashMap;

public class Epic extends Task {
    protected final HashMap<Integer, SubTask> epicSubTasks = new HashMap<>();
//    private final TaskTypes taskType = TaskTypes.EPIC;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskTypes.EPIC;
    }

    public Epic(int ID, String name, String description) {
        super(ID, name, description);
        this.type = TaskTypes.EPIC;
    }

    public Epic(int ID, String name, String description, Status status) {
        super(ID, name, description, status);
        this.type = TaskTypes.EPIC;
    }

    public Epic(int ID, String name, String description, Status status, ZonedDateTime startDateTime, long duration) {
        super(ID, name, description, status, startDateTime, duration);
        this.type = TaskTypes.EPIC;
    }

    public HashMap<Integer, SubTask> getEpicSubTasks() {
        return epicSubTasks;
    }

    @Override
    public TaskTypes getType() {
        return type;
    }

    @Override
    public String toString() {
        return System.lineSeparator() +
                "[Эпик: " + getName() + "] " +
                "[ID: " + getID() + "] " +
                "[Cтатус: " + getStatus() + "] " +
                "[Подзадачи: " + getEpicSubTasks().size() + "] " +
                "[Начало: " + printStartTime() + "] " +
                "[Окончание: " + printEndTime() + "] " +
                "[Длительность: " + printDuration() + "]";
    }
}