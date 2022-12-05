package ru.yandex.practicum.tasks;

import ru.yandex.practicum.managers.ManagerSaveException;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    public static Status convertToEnum(String status) {
        Status enumStatus = null;
        for (Status elem : Status.values()) {
            if (elem.name().equals(status)){
                enumStatus = elem;
            }
            break;
        }
        return enumStatus;
    }
}