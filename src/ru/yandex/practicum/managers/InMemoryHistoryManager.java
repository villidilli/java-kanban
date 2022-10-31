package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

	private final List<Task> browsingHistory = new ArrayList<>();

	@Override
	public void add(Task task) {
		if (task != null) {
			if (browsingHistory.size() == 10) {
				browsingHistory.remove(0);
			}
			browsingHistory.add(task);
		} else {
			System.out.println("[Ошибка] Входящий объект = null");
		}
	}

	@Override
	public List<Task> getHistory() {
		return browsingHistory;
	}
}