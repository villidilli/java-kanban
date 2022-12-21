package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskTypes;

import java.util.List;
import java.util.Map;

public interface HistoryManager {

	void add(Task task);

	void remove(int id);

	void deleteAllTasksByType(Map<Integer, ? extends Task> tasks);

	List<Task> getHistory();
}