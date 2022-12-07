package ru.yandex.practicum.utils;

import ru.yandex.practicum.managers.HistoryManager;

import ru.yandex.practicum.tasks.*;

import java.util.ArrayList;
import java.util.List;

public class TaskConverter {

    public static String taskToString(Task task) {
        TaskTypes taskType = task.getTaskType();
        String result = null;

        switch (taskType) {
            case TASK:
            case EPIC:
                result = task.getID() + "," +
                        task.getTaskType().name() + "," +
                        task.getName() + "," +
                        task.getStatus() + "," +
                        task.getDescription() + ",";
                break;
            case SUBTASK:
                result = task.getID() + "," +
                        task.getTaskType().name() + "," +
                        task.getName() + "," +
                        task.getStatus() + "," +
                        task.getDescription() + "," +
                        ((SubTask) task).getParentEpicID();
                break;
        }
        return result;
    }

    public static String historyToString(HistoryManager historyManager) {
        List<Task> history = historyManager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();
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

    public static Task taskFromString(String value) {
        Task task = null;
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

    public static List<Integer> historyFromString(String value) {
        //рассплитили строку на массив ID
        String[] arrayID = value.split(",");
        List<Integer> listHistoryID = new ArrayList<>();
        //приводим стринговое ID к инту и сохраняем в список
        for (int i = 0; i < arrayID.length; i++) {
            listHistoryID.add(Integer.parseInt(arrayID[i]));
        }
        return listHistoryID;
    }
}