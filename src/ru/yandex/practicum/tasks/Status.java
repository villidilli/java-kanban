package ru.yandex.practicum.tasks;

import ru.yandex.practicum.managers.ManagerSaveException;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    public static Status convertToEnum(String status) throws ManagerSaveException {
        Status enumStatus = null;
        for (Status elem : Status.values()) {
            if (elem.name().equals(status)){
                enumStatus = elem;
            } else {
                throw new ManagerSaveException("Ошибка -> Не найден элемент Enum равный типу из файла");
            }
        }
        return enumStatus;
    }
}