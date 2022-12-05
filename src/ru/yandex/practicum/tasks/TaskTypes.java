package ru.yandex.practicum.tasks;

import ru.yandex.practicum.managers.ManagerSaveException;
import ru.yandex.practicum.tasks.*;

public enum TaskTypes {
    TASK,
    SUBTASK,
    EPIC;

    public static TaskTypes convertToEnum(String type) throws ManagerSaveException {
        TaskTypes taskType = null;
        for (TaskTypes elem : TaskTypes.values()) {
            if (elem.name().equals(type)){
                taskType = elem;
            } else {
                throw new ManagerSaveException("Ошибка -> Не найден элемент Enum равный типу из файла");
            }
        }
        return taskType;
    }
}
