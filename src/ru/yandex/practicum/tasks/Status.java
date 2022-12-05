package ru.yandex.practicum.tasks;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    public static Status convertStatusToEnum(String status) {
        Status enumStatus = null;
        for (Status elem : Status.values()) {
            if (elem.name().equals(status)){
                enumStatus = elem;
                break;
            }
        }
        return enumStatus;
    }
}