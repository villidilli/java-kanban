package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    //	private static final int MAX_HISTORY_LENGTH = 10;
    private int historySize = 0;
    private Node<Task> historyTail = null;
    private Node<Task> historyHead = null;

    //объявил не через интерфейс чтобы был доступ к методам LinkedList
//	private final LinkedList<Task> browsingHistory = new LinkedList<>();

    private final Map<Integer, Node<Task>> history = new HashMap<>();

    private void linkLast(Task task) {
        Node node = new Node(historyTail, task);
        historyTail = node;

        // если история пустая, тогда голова будет равняться хвосту
        if (historySize == 0) {
            historyHead = historyTail;
        }
        if (history.containsKey(task.getID())) {
            history.put(task.getID(), node);
            remove(task.getID());
        }
        history.put(task.getID(), node);
        historySize++;
    }

    private void removeNode(Node<Task> deletingNode) {
        boolean isHead = historyHead == deletingNode; // удаляемая нода - голова?
        boolean isHeadAndTail = historySize == 1; // удаляемая нода единственная? голова == хвост

        /*
         * Не описано условие в ветлении когда и голова и хвост одна и та же нода (аналог historySize == 1)
         * так как при таком условии достаточно просто удалить ноду и все ссылки головы и хвоста станут Null
         */
        if (historyHead == deletingNode) { // если голова ссылается на удаляемую ноду
            historyHead = deletingNode.next; // головой становится следующая нода
        } else {
            // если голова и хвост не являются удаляемой нодой, тогда меняем ссылки
            deletingNode.prev.next = deletingNode.next;
            deletingNode.next.prev = deletingNode.prev;
        }
        history.remove(deletingNode);
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> nextNode = historyHead; // первый элемент - нода головы

        do {
            historyList.add(nextNode.task); // добавили в список задачу из ноды
            nextNode = nextNode.next; // перешли к другой ноде
        } while (nextNode != null); // пока не прилетит null с хвоста

        return historyList;
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    @Override
    public void add(Task task) {
        if (task != null) {
//			if (browsingHistory.size() == MAX_HISTORY_LENGTH) {
//				browsingHistory.removeFirst();
//			}
//			browsingHistory.addLast(task)
            linkLast(task);
        } else {
            System.out.println("[Ошибка] Входящий объект = null");
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private static class Node<Task> {
        Node<Task> prev = null;
        Node<Task> next = null;
        Task task;


        Node(Node<Task> prev, Task task) {
            this.prev = prev;
            this.task = task;
        }


    }
}