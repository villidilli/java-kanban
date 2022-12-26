package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskTypes;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class InMemoryHistoryManager implements HistoryManager {
	private final Map<Integer, Node<Task>> history = new HashMap<>();
	private Node<Task> historyTail = null;
	private Node<Task> historyHead = null;

	private void linkLast(Task task) {
		//т.к. мы добавляем в конец списка, то ссылка next у ноды будет всегда null (инициализируется в конструкторе)
		Node node = new Node(historyTail, task, null);

		//если голова пустая, значит в истории нет объектов, тогда голова = новой ноде
		if (historyHead == null) {
			historyHead = node;
		} else {
			//в противном случае старой ноде-хвосту присваиваем ссылку на след.ноду = новой ноде
			historyTail.next = node;
		}
		//новая нода всегда будет хвостом
		historyTail = node;
	}

	private void removeNode(Node<Task> node) {
		if (node != null) { // иначе бросает NPE
			if (node.next == null && node.prev == null) { // это единственная нода в связке
				historyTail = null;
				historyHead = null;
			} else if (node.next == null) { // это хвост и есть ещё элементы в связке
				historyTail = node.prev;
				historyTail.next = null;
			} else if (node.prev == null) { // это голова и есть ещё элементы в связке
				historyHead = node.next;
				historyHead.prev = null;
			} else { // значит нода из середины, то меняем ссылки соседних нод друг на друга
				node.prev.next = node.next;
				node.next.prev = node.prev;
			}
			history.remove(node.task.getID()); // удаляем саму ноду
		}
	}

	private List<Task> getTasks() {
		List<Task> historyList = new ArrayList<>();
		//движемся по ссылкам next каждой ноды, начиная с головы
		Node<Task> currentNode = historyHead; // переменная в роли курсора, перед какой нодой стоим
		while (currentNode != null) { // пока не дошли до ссылки на null у хвоста
			historyList.add(currentNode.task);
			currentNode = currentNode.next; // передвинули курсор на след.ноду
		}
		return historyList;
	}

	@Override
	public void deleteAllTasksByType(Map<Integer, ? extends Task> tasks) {
		tasks.keySet().forEach(taskID -> removeNode(history.get(taskID)));
	}

	@Override
	public void remove(int id) {
		removeNode(history.get(id));
	}

	@Override
	public void add(Task task) {
		if (task == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}
		remove(task.getID()); //удаляет старую ноду, чтобы в связке не было дублей-ссылок
		linkLast(task);
		history.put(task.getID(), historyTail);
	}

	@Override
	public List<Task> getHistory() {
		return getTasks();
	}

	static class Node<Task> {
		Node<Task> prev;
		Node<Task> next;
		Task task;

		Node(Node<Task> prev, Task task, Node<Task> next) {
			this.prev = prev;
			this.task = task;
			this.next = next;
		}
	}
}