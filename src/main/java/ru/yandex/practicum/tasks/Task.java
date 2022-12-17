package ru.yandex.practicum.tasks;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {
	private int ID;
	private String name;
	private String description;
	private ru.yandex.practicum.tasks.Status status = Status.NEW;
	private final TaskTypes taskType = TaskTypes.TASK;
	private ZonedDateTime startTime;
	private Duration duration;

	public Task(String name, String description) {
		this.description = description;
		this.name = name;
	}

	public Task(int ID, String name, String description) {
		this.ID = ID;
		this.name = name;
		this.description = description;
	}

	public Task(int ID, String name, String description,
				int year, int month, int day,
				int hour, int minutes, int duration) {
		this.ID = ID;
		this.name = name;
		this.description = description;
		this.duration = Duration.of(duration, ChronoUnit.MINUTES);
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
				int hour, int minutes, int duration
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

	public ZonedDateTime getEndTime() {
		if (startTime == null || duration == null) {
			throw new TimeValueException("Невозможно рассчитать время окончания." +
										"Проверьте значения операндов");
		}
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

	public ZonedDateTime getStartTime() {
		return startTime;
	}

	public int getDuration() {
		if (duration == null || duration.isNegative()) {
			throw new TimeValueException("Продолжительность недоступна. Значение = null или отрицательное");
		}
		return (int) duration.toMinutes();
	}

	@Override
	public String toString() {
		return "\n[Задача: " + getName() + "] " + "[ID: " + getID() + "] " + "[Cтатус: " + getStatus() + "] " + "[Описание: " + getDescription() + "] ";
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
}