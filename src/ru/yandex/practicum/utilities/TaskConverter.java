package ru.yandex.practicum.utilities;

import ru.yandex.practicum.managers.HistoryManager;
import ru.yandex.practicum.managers.ManagerSaveException;
import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.tasks.TaskTypes;

import java.util.ArrayList;
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

    public static String historyToString(HistoryManager historyManager) {
        List<Task> history = historyManager.getHistory();
        stringBuilder = new StringBuilder();
        // проходим через fori, чтобы у последнего id не ставить запятую
        for (int i = 0; i < history.size(); i++) {
            if (i != history.size() - 1) {
                stringBuilder.append(history.get(i).getID() + ",");
            } else {
                stringBuilder.append(history.get(i).getID());
            }
        }
        return stringBuilder.toString();
    }

//    public static String historyToString(List <Task> list) {
//        stringBuilder = new StringBuilder();
//        // проходим через fori, чтобы у последнего id не ставить запятую
//        for (int i = 0; i < list.size(); i++) {
//            if (i != list.size() - 1) {
//                stringBuilder.append(list.get(i).getID() + ",");
//            } else {
//                stringBuilder.append(list.get(i).getID());
//            }
//        }
//        return stringBuilder.toString();
//    }

    public static Task taskFromString(String value) {
        Task task = null;
        String[] fields = value.split(",");

        int ID = Integer.parseInt(fields[0]);
        TaskTypes type = TaskTypes.convertToEnum(fields[1]);
        String name = fields[2];
        Status status = Status.convertToEnum(fields[3]);
        String description = fields[4];

        switch (type) {
            case TASK:
                task = new Task(ID, name, description, status);
                break;
            case SUBTASK:
                int parentEpicID = Integer.parseInt(fields[5]);
                task = new SubTask(ID, name, description, parentEpicID);
                task.setStatus(status);
                break;
            case EPIC:
                task = new Epic(ID, name, description);
                task.setStatus(status);
                break;
        }
        return task;
    }
}
