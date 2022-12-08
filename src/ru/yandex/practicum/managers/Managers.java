package ru.yandex.practicum.managers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

	public static TaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}

	public static FileBackedTasksManager getDefaultFileBacked() {
		return FileBackedTasksManager.loadFromFile(new File("resources/Backup.csv"));
	}
}