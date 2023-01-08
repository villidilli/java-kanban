package ru.yandex.practicum.managers;

import ru.yandex.practicum.api.HttpException;
import ru.yandex.practicum.api.KVTaskClient;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.File;
import java.io.IOException;

import static ru.yandex.practicum.managers.HttpManagerKey.*;

public class HttpTaskManager extends FileBackedTasksManager{
    private static String serverURL;

    public HttpTaskManager(String url) {
        serverURL = url;
    }

    public static HttpTaskManager load(String url) throws IOException, InterruptedException, HttpException {
        serverURL = url;
        final HttpTaskManager httpManager = new HttpTaskManager(serverURL);
        final KVTaskClient client = new KVTaskClient(serverURL);
        client.load(TASKS.name());
        client.load(SUBTASKS.name());
        client.load(EPICS.name());
        client.load(HISTORY.name());
        client.load(PRIORITIZED_LIST.name());
        return httpManager;
    }

    @Override
    public void create(Task newTask) {
        super.create(newTask);
    }

    @Override
    public void create(SubTask newSubTask) {
        super.create(newSubTask);
    }

    @Override
    public void create(Epic newEpic) {
        super.create(newEpic);
    }

    @Override
    public void update(Task newTask) {
        super.update(newTask);
    }

    @Override
    public void update(SubTask newSubTask) {
        super.update(newSubTask);
    }

    @Override
    public void update(Epic newEpic) {
        super.update(newEpic);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
    }

    @Override
    public Task getTaskByID(int ID) {
        return super.getTaskByID(ID);
    }

    @Override
    public SubTask getSubTaskByID(int ID) {
        return super.getSubTaskByID(ID);
    }

    @Override
    public Epic getEpicByID(int ID) {
        return super.getEpicByID(ID);
    }

    @Override
    public void deleteTaskByID(int ID) {
        super.deleteTaskByID(ID);
    }

    @Override
    public void deleteSubTaskByID(int ID) {
        super.deleteSubTaskByID(ID);
    }

    @Override
    public void deleteEpicByID(int ID) {
        super.deleteEpicByID(ID);
    }
}
