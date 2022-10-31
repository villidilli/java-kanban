package ru.yandex.practicum.tasks;

import java.util.HashMap;

public class Epic extends Task {

    private HashMap<Integer, SubTask> epicSubTasks = new HashMap<>();

    /*
     * ЛЕГЕНДА:
     * Первое вхождение объекта обязательно без ID
     * Повторные вхождения всегда с ID, но всегда без указания STATUS,
     * т.к. рассчитывается на основании статусов подзадач
     */

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int ID, String name, String description) {
        super(ID, name, description);
    }

    public HashMap<Integer, SubTask> getEpicSubTasks() {
        return epicSubTasks;
    }

    @Override
    public String toString() {
        return "[Эпик: " + getName() + "] " +
                "[ID: " + getID() + "] " +
                "[Cтатус: " + getStatus() + "] " +
                "[Описание: " + getDescription() + "]" +
                "[Подзадачи: " + getEpicSubTasks().size() + "] ";
    }
}