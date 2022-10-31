package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;


import java.util.List;


public interface TaskManager {

    void create (Task newTask);

    void create(SubTask newSubTask);

    void create(Epic newEpic);

    void update(Task newTask);

    void update(SubTask newSubTask);

    void update(Epic newEpic);

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpics();

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpics();

    Task getTaskByID(int ID);

    SubTask getSubTaskByID(int ID);

    Epic getEpicByID(int ID);

    void deleteTaskByID(int ID);

    void deleteSubTaskByID(int ID);

    void deleteEpicByID(int ID);

    List<SubTask> getAllSubTasksByEpic(int ID);

    List<Task> getHistory();
}