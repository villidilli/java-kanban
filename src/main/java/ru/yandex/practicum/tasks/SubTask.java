package ru.yandex.practicum.tasks;

import java.time.ZonedDateTime;

public class SubTask extends Task {

    private int parentEpicID;
//    private final TaskTypes taskType = TaskTypes.SUBTASK;

    public SubTask(String name, String description, int parentEpicID) {
        super(name, description);
        this.parentEpicID = parentEpicID;
        this.type = TaskTypes.SUBTASK;
    }

    public SubTask(String name, String description, int parentEpicID,
                   ZonedDateTime startDateTime, long duration) {
        super(name, description, startDateTime, duration);
        this.parentEpicID = parentEpicID;
        this.type = TaskTypes.SUBTASK;
    }

    //ниже конструкторы для повторных вхождений
    public SubTask(int ID, String name, String description, int parentEpicID) {
        super(ID, name, description);
        this.parentEpicID = parentEpicID;
        this.type = TaskTypes.SUBTASK;
    }

    public SubTask(int ID, String name, String description, int parentEpicID,
                   ZonedDateTime startDateTime, long duration) {
        super(ID, name, description, startDateTime, duration);
        this.parentEpicID = parentEpicID;
        this.type = TaskTypes.SUBTASK;
    }

    public SubTask(int ID, String name, String description, Status status, int parentEpicID) {
        super(ID, name, description, status);
        this.parentEpicID = parentEpicID;
        this.type = TaskTypes.SUBTASK;
    }

    public SubTask(int ID, String name, String description, Status status, int parentEpicID,
                   ZonedDateTime startDateTime, long duration) {
        super(ID, name, description, status, startDateTime, duration);
        this.parentEpicID = parentEpicID;
        this.type = TaskTypes.SUBTASK;
    }

    @Override
    public Integer getParentEpicID() {
        return parentEpicID;
    }

    public void setParentEpicID(int id) {
        this.parentEpicID = id;
    }

    @Override
    public TaskTypes getType() {
        return type;
    }

    @Override
    public String toString() {
        return System.lineSeparator() +
                "[Подзадача: " + getName() + "] " +
                "[ID: " + getID() + "] " +
                "[Cтатус: " + getStatus() + "] " +
                "[Эпик ID: " + getParentEpicID() + "] " +
                "[Начало: " + printStartTime() + "] " +
                "[Окончание: " + printEndTime() + "] " +
                "[Длительность: " + printDuration() + "]";
    }
}