package ru.yandex.practicum.utils;

import ru.yandex.practicum.managers.HistoryManager;

import ru.yandex.practicum.tasks.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class TaskConverter {
	public static final String LINE_SEPARATOR = "\\r?\\n";

	public static String taskToString(Task task) {
		return task.getID() + "," +
				task.getTaskType().name() + "," +
				task.getName() + "," +
				task.getStatus() + "," +
				task.getDescription() + "," +
				task.getParentEpicID() + "," +
				TimeConverter.dateTimeToString(task.getStartTime()) + "," +
				task.getDuration();
	}

	public static String historyToString(HistoryManager historyManager) {
		List<Task> history = historyManager.getHistory();
		StringBuilder stringBuilder = new StringBuilder();
		if (history.isEmpty()) {
			return "";
		}
		stringBuilder.append(history.get(0).getID());
		for (int i = 1; i < history.size(); i++) {
			stringBuilder.append(",");
			stringBuilder.append(history.get(i).getID());
		}
		return stringBuilder.toString();
	}

	public static Task taskFromString(String value) {
		//рассплитили строку по полям
		String[] fields = value.split(",");
		//инициализируем поля для создания объекта задач через конструкторы
		int ID = Integer.parseInt(fields[0]);
		TaskTypes type = TaskTypes.valueOf(fields[1]);
		String name = fields[2];
		Status status = Status.valueOf(fields[3]);
		String description = fields[4];
		ZonedDateTime startTime = TimeConverter.dateTimeFromString(fields[6]);
		long duration = TimeConverter.durationFromString(fields[7]);

		switch (type) {
			case TASK:
				return new Task(ID, name, description, status, startTime, duration);
			case SUBTASK:
				int parentEpicID = Integer.parseInt(fields[5]);
				return new SubTask(ID, name, description, status, parentEpicID, startTime, duration);
		}
		return new Epic(ID, name, description, status, startTime, duration);
	}

	public static List<Integer> historyFromString(String value) {
		//рассплитили строку на массив ID
		String[] arrayID = value.split(",");
		List<Integer> listHistoryID = new ArrayList<>();
		//приводим стринговое ID к инту и сохраняем в список
		for (String ID : arrayID) {
			listHistoryID.add(Integer.parseInt(ID));
		}
		return listHistoryID;
	}
}