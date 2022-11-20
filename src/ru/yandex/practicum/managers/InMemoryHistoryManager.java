package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> historyTail = null;
    private Node<Task> historyHead = null;

    private void linkLast(Task task) {
        //т.к. мы добавляем в конец списка, то ссылка next у ноды будет всегда null (инициализируется в конструкторе)
        Node node = new Node(historyTail, task);

        // если история пустая, тогда голова будет равняться хвосту
        if (history.isEmpty()) {
            historyTail = node;
            historyHead = historyTail;
        }
        // если есть хотя бы 1 элемент, значит хвост имеет ноду
        if (history.size() > 0) {
            historyTail.next = node;
            historyTail = node;
        }
        //если история содержит добавляемую ноду, старую удаляем, новую добавляем чтобы встала в хвост связки
        if (history.containsKey(task.getID())) {
            remove(task.getID());
        }
        history.put(task.getID(), node);
    }

    private void removeNode(Node<Task> deletingNode) {
        if (deletingNode != null) { // иначе бросает NPE
            Node<Task> nextNode = deletingNode.next;
            Node<Task> prevNode = deletingNode.prev;

            boolean isHead = historyHead == deletingNode; // удаляемая нода - голова?
            boolean isTail = historyTail == deletingNode; // удаляемая нода - хвост?

            // если удаляем ноду-голову и есть ещё ноды в связке
            if (isHead && nextNode != null) {
                nextNode.prev = prevNode; // у следующей ноды ссылку ставим на предыдущую ноду
                historyHead = nextNode; // головой = следующая нода
            }
            // если удаляем ноду-хвост и есть ещё ноды в связке
            if (isTail && prevNode != null) {
                prevNode.next = nextNode; // У пред.ноды ссылку ставим на следующую ноду
                historyTail = prevNode; // хвост = предыдущая нода
            }
            // если удаляем из середины связки (не голова и не хвост)
            if (!isHead && !isTail) {
                // т.к. это середина связки, просто меняем ссылки
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
            // если удаляем единственную ноду связи (голова=хвост)
            if (isHead && isTail) {
                // обнуляем ссылки, иначе не корректно работает getTasks() для getHistory()
                historyHead = null;
                historyTail = null;
            }
            history.remove(deletingNode.task.getID()); // удаляем саму ноду
        }
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();

        if (history.size() != 0){
            //движемся по ссылкам next каждой ноды, начиная с головы
            Node<Task> currentNode = historyHead; // переменная в роли курсора, перед какой нодой стоим
            while (currentNode != null) { // пока не дошли до ссылки на null у хвоста
                historyList.add(currentNode.task);
                currentNode = currentNode.next; // передвинули курсор на след.ноду
            }
        }
        return historyList;
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            linkLast(task);
        } else {
            System.out.println("[Ошибка] Входящий объект = null");
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private class Node<Task> {
        Node<Task> prev = null;
        Node<Task> next = null;
        Task task;

        // private - чтобы ноду нельзя было создать из-вне
        private Node(Node<Task> prev, Task task) {
            this.prev = prev;
            this.task = task;
        }
    }
}