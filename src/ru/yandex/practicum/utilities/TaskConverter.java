package ru.yandex.practicum.utilities;

import ru.yandex.practicum.managers.ManagerSaveException;
import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.tasks.TaskTypes;

import java.util.List;

public class TaskConverter {
    private static StringBuilder stringBuilder;

    private TaskConverter() {}

    public static String taskToString(Task task) {
        stringBuilder = new StringBuilder();

        // добавляем поля, которые не привязываны к типу класса
        stringBuilder.append(task.getID() + ",");
        stringBuilder.append(task.getName() + ",");
        stringBuilder.append(task.getStatus() + ",");
        stringBuilder.append(task.getDescription() + ",");

        // добавляем название класса = константе enum
        if (task.getClass() == Task.class) {
            stringBuilder.insert(2, TaskTypes.TASK + ",");
        } else if (task.getClass() == SubTask.class) {
            stringBuilder.insert(2, TaskTypes.SUBTASK + ",");
            stringBuilder.append(((SubTask) task).getParentEpicID());
        } else if (task.getClass() == Epic.class) {
            stringBuilder.insert(2, TaskTypes.EPIC + ",");
        } else {
            throw new ManagerSaveException("Ошибка -> Неизвестный тип класса");
        }
        return stringBuilder.toString();
    }

    public static String historyToString(List <? extends Task> list) {
        stringBuilder = new StringBuilder();
        // проходим через fori, чтобы у последнего id не ставить запятую
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                stringBuilder.append(list.get(i).getID() + ",");
            } else {
                stringBuilder.append(list.get(i).getID());
            }
        }
        return stringBuilder.toString();
    }

    public static Task taskFromString(String value) {
        return new Task("1", "2");
    }
}
