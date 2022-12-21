package ru.yandex.practicum.tasks;

import ru.yandex.practicum.utils.TimeConverter;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class Epic extends Task {
	private final HashMap<Integer, SubTask> epicSubTasks = new HashMap<>();
	private final TaskTypes taskType = TaskTypes.EPIC;



	public Epic(String name, String description) {
		super(name, description);
	}

	public Epic(int ID, String name, String description) {
		super(ID, name, description);
	}

	public Epic(int ID, String name, String description, Status status) {
		super(ID, name, description, status);
	}

	public Epic(int ID, String name, String description, Status status, ZonedDateTime zonedDateTime, Duration duration) {
		super(ID, name, description, status, zonedDateTime, duration);
	}

	public HashMap<Integer, SubTask> getEpicSubTasks() {
		return epicSubTasks;
	}

	@Override
	public TaskTypes getTaskType() {
		return taskType;
	}

	@Override
	public String toString() {
		return "\n[Эпик: " + getName() + "] " +
				"[ID: " + getID() + "] " +
				"[Cтатус: " + getStatus() + "] " +
				"[Подзадачи: " + getEpicSubTasks().size() + "] " +
				"[Начало: " + printStartTime() + "] " +
				"[Окончание: " + printEndTime() + "] " +
				"[Длительность: " + printDuration() + "]";
	}
}