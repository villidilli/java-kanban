package ru.yandex.practicum.managers;


import java.io.File;

public class Managers {

	public static HttpTaskManager getDefault() {
		return new HttpTaskManager("http://localhost:8078");
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}

	public static FileBackedTasksManager getDefaultFileBacked() {
		return FileBackedTasksManager.load(new File("src/main/resources/Backup.csv"));
	}
}