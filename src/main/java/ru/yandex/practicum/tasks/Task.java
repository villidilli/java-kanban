package ru.yandex.practicum.tasks;

import ru.yandex.practicum.utils.TimeConverter;

import java.time.*;
import java.util.Objects;

public class Task {
    public static final ZonedDateTime UNREACHEBLE_DATE = ZonedDateTime.of(
            9999, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
    private final TaskTypes taskType = TaskTypes.TASK;
    private int ID;
    private String name;
    private String description;
    private Status status = Status.NEW;
    private ZonedDateTime startTime = UNREACHEBLE_DATE;
    private Duration duration = Duration.ZERO;

    public Task(String name, String description) {
        this.description = description;
        this.name = name;
    }

    public Task(String name, String description,
                int year, int month, int day,
                int hour, int minutes, long duration) {
        this.description = description;
        this.name = name;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = ZonedDateTime.of(
                LocalDate.of(year, month, day),
                LocalTime.of(hour, minutes),
                ZoneId.systemDefault()
        );
    }

    public Task(int ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    public Task(int ID, String name, String description,
                int year, int month, int day,
                int hour, int minutes, long duration) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = ZonedDateTime.of(
                LocalDate.of(year, month, day),
                LocalTime.of(hour, minutes),
                ZoneId.systemDefault()
        );
    }

    public Task(int ID, String name, String description, Status status) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int ID, String name, String description, Status status,
                int year, int month, int day,
                int hour, int minutes, long duration
    ) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = ZonedDateTime.of(
                LocalDate.of(year, month, day),
                LocalTime.of(hour, minutes),
                ZoneId.systemDefault()
        );
    }

    public Task(int ID, String name, String description, Status status, ZonedDateTime zonedDateTime, long duration) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = zonedDateTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime dateTime) {
        if (dateTime != UNREACHEBLE_DATE) {
            startTime = dateTime;
        }
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    public void setDuration(long minutes) {
        if (minutes <= 0) {
            throw new TimeValueException("Значение продолжительности должно быть больше 0");
        }
        duration = Duration.ofMinutes(minutes);
    }

    public ZonedDateTime getEndTime() {
        return startTime.plusMinutes(getDuration());
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

    public TaskTypes getTaskType() {
        return taskType;
    }

    public Integer getParentEpicID() {
        return null;
    }

    @Override
    public String toString() {
        return "\n[Задача: " + getName() + "] " +
                "[ID: " + getID() + "] " +
                "[Cтатус: " + getStatus() + "] " +
                "[Начало: " + printStartTime() + "]" +
                "[Окончание: " + printEndTime() + "]" +
                "[Длительность: " + printDuration() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return ID == task.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public String printEndTime() {
        if (startTime != UNREACHEBLE_DATE && !duration.isZero()) {
            return TimeConverter.dateTimeToString(getEndTime());
        }
        return "--";
    }

    public String printDuration() {
        if (!duration.isZero()) {
            return String.valueOf(duration.toMinutes());
        }
        return "--";
    }

    public String printStartTime() {
        if (startTime == UNREACHEBLE_DATE) {
            return "--";
        }
        return TimeConverter.dateTimeToString(startTime);
    }
}