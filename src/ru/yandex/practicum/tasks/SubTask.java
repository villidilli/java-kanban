package ru.yandex.practicum.tasks;

public class SubTask extends Task {

    private int parentEpicID;

    /*
     * ЛЕГЕНДА:
     * Первое вхождение объекта обязательно без ID
     * Повторные вхождения всегда с ID
     */

    // конструктор для 1-ого вхождения
    public SubTask(String name, String description, int parentEpicID) {
        super(name, description);
        this.parentEpicID = parentEpicID;
    }

    //ниже конструкторы для повторных вхождений
    public SubTask(int ID, String name, String description, int parentEpicID) {
        super(ID, name, description);
        this.parentEpicID = parentEpicID;
    }

    public SubTask(int ID, String name, String description, Status status, int parentEpicID) {
        super(ID, name, description, status);
        this.parentEpicID = parentEpicID;
    }

    public int getParentEpicID() {
        return parentEpicID;
    }

    @Override
    public String toString() {
        return "[Подзадача: " + getName() + "] " +
                "[ID: " + getID() + "] " +
                "[Cтатус: " + getStatus() + "] " +
                "[Описание: " + getDescription() + "]" +
                "[Эпик ID: " + getParentEpicID() + "] ";
    }
}