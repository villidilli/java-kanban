package ru.yandex.practicum.managers;

import java.io.File;

public class Managers {

	public static TaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}

	public static FileBackedTasksManager getDefaultFileBacked() {
		return FileBackedTasksManager.loadFromFile(
				new File("D:\\JavaDev\\java-kanban\\resources\\Backup.csv"));
	}
}