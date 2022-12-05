package ru.yandex.practicum.tasks;

public enum TaskTypes {
    TASK,
    SUBTASK,
    EPIC;

    public static TaskTypes convertTypeToEnum(String type) {
        TaskTypes taskType = null;
        for (TaskTypes elem : TaskTypes.values()) {
            if (elem.name().equals(type)){
                taskType = elem;
                break;
            }
        }
        return taskType;
    }
}