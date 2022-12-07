package ru.yandex.practicum.utils;

import ru.yandex.practicum.managers.HistoryManager;

import ru.yandex.practicum.tasks.*;

import java.util.ArrayList;
import java.util.List;

public class TaskConverter {

    public static String taskToString(Task task) {
        TaskTypes taskType = task.getTaskType();
        if (taskType != TaskTypes.SUBTASK) {
            return task.getID() + "," +
                    taskType.name() + "," +
                    task.getName() + "," +
                    task.getStatus() + "," +
                    task.getDescription() + ",";
        }
        return task.getID() + "," +
                taskType.name() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getParentEpicID();
    }

    public static String historyToString(HistoryManager historyManager) {
        List<Task> history = historyManager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();
        // проходим через fori, чтобы у последнего id не ставить запятую
        for (int i = 0; i < history.size(); i++) {
            int ID = history.get(i).getID();
            if (i != history.size() - 1) {
                stringBuilder.append(ID + ",");
            } else {
                stringBuilder.append(ID);
            }
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

        switch (type) {
            case TASK:
                return new Task(ID, name, description, status);
            case SUBTASK:
                int parentEpicID = Integer.parseInt(fields[5]);
                return new SubTask(ID, name, description, status, parentEpicID);

        }
        return new Epic(ID, name, description, status);
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