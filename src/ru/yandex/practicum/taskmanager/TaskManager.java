package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    void reCheckEpicStatus(int epicID);

    void create(Task newTask);

    void create(SubTask newSubTask);

    void create(Epic newEpic);

    void update(Task newTask);

    void update(SubTask newSubTask);

    void update(Epic newEpic);

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpics();

    Task getTaskByID(int ID);

    SubTask getSubTaskByID(int ID);

    Epic getEpicByID(int ID);

    void deleteTaskByID(int ID);

    void deleteSubTaskByID(int ID);

    void deleteEpicByID(int ID);

    ArrayList<SubTask> getAllSubTasksByEpic(int ID);
}