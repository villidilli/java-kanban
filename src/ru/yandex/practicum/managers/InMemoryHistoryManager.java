package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
	private static final int MAX_HISTORY_LENGTH = 10;

	//объявил не через интерфейс чтобы был доступ к методам LinkedList
	private final LinkedList<Task> browsingHistory = new LinkedList<>();

	@Override
	public void add(Task task) {
		if (task != null) {
			if (browsingHistory.size() == MAX_HISTORY_LENGTH) {
				browsingHistory.removeFirst();
			}
			browsingHistory.addLast(task);
		} else {
			System.out.println("[Ошибка] Входящий объект = null");
		}
	}

	@Override
	public List<Task> getHistory() {
		return browsingHistory;
	}
}