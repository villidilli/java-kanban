package ru.yandex.practicum.tasks;

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
				"[Описание: " + getDescription() + "]" +
				"[Подзадачи: " + getEpicSubTasks().size() + "] ";
	}
}