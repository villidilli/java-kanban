package ru.yandex.practicum.tasks;

public class SubTask extends Task {

	private int parentEpicID;
	private TaskTypes taskType = TaskTypes.SUBTASK;

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