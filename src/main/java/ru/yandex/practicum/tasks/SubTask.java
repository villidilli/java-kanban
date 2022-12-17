package ru.yandex.practicum.tasks;

public class SubTask extends Task {

	private final int parentEpicID;
	private final TaskTypes taskType = TaskTypes.SUBTASK;

	public SubTask(String name, String description, int parentEpicID) {
		super(name, description);
		this.parentEpicID = parentEpicID;
	}

	//ниже конструкторы для повторных вхождений
	public SubTask(int ID, String name, String description, int parentEpicID) {
		super(ID, name, description);
		this.parentEpicID = parentEpicID;
	}

	public SubTask(int ID, String name, String description, int parentEpicID,
				   int year, int month, int day,
				   int hour, int minutes, int duration) {
		super(ID, name, description, year, month, day, hour, minutes, duration);
		this.parentEpicID = parentEpicID;
	}

	public SubTask(int ID, String name, String description, Status status, int parentEpicID) {
		super(ID, name, description, status);
		this.parentEpicID = parentEpicID;
	}

	public SubTask(int ID, String name, String description, Status status, int parentEpicID,
				   int year, int month, int day,
				   int hour, int minutes, int duration) {
		super(ID, name, description, status, year, month, day, hour, minutes, duration);
		this.parentEpicID = parentEpicID;
	}

	@Override
	public Integer getParentEpicID() {
		return parentEpicID;
	}

	@Override
	public TaskTypes getTaskType() {
		return taskType;
	}

	@Override
	public String toString() {
		return "\n[Подзадача: " + getName() + "] " +
				"[ID: " + getID() + "] " +
				"[Cтатус: " + getStatus() + "] " +
				"[Описание: " + getDescription() + "]" +
				"[Эпик ID: " + getParentEpicID() + "] ";
	}
}