package ru.yandex.practicum.tasks;

public class Task {
    private int ID;
    private String name;
    private String description;
    private Status status = Status.NEW;

    /*
     * ЛЕГЕНДА:
     * Первое вхождение объекта обязательно без ID
     * Повторные вхождения всегда с ID
     */

    public Task(String name, String description) {
        this.description = description;
        this.name = name;
    }

    public Task(int ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    public Task(int ID, String name, String description, Status status) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) { // сеттер необходим для работы метода create()
            this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "[Задача: " + getName() + "] " +
                "[ID: " + getID() + "] " +
                "[Cтатус: " + getStatus() + "] " +
                "[Описание: " + getDescription() + "]";
    }
}