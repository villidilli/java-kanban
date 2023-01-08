package ru.yandex.practicum.managers;

import com.google.gson.Gson;
import ru.yandex.practicum.api.HttpException;
import ru.yandex.practicum.api.KVTaskClient;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import com.google.gson.*;
import ru.yandex.practicum.utils.EpicToJsonConverter;
import ru.yandex.practicum.utils.SubtaskToJsonConverter;
import ru.yandex.practicum.utils.TaskToJsonConverter;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;


import static ru.yandex.practicum.managers.HttpManagerKey.*;

public class HttpTaskManager extends FileBackedTasksManager{
    private static String serverURL;
    private final KVTaskClient kvClient;
    private final Gson gson;

    private final FileBackedTasksManager backedManager;

    public HttpTaskManager(String url) {
        serverURL = url;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Task.class, new TaskToJsonConverter())
                .registerTypeAdapter(SubTask.class, new SubtaskToJsonConverter())
                .registerTypeAdapter(Epic.class, new EpicToJsonConverter())
                .create();
        backedManager = new FileBackedTasksManager();
        try {
            kvClient = new KVTaskClient(url);
            load();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void load()  {
        restoreTasks();
        restoreHistory();
        restorePrioritizedTasks();
        restoreID();
    }

    private void restoreID() {
        tasks.values().forEach(this::updateGeneratorID);
        subTasks.values().forEach(this::updateGeneratorID);
        epics.values().forEach(this::updateGeneratorID);
    }

    private void restoreTasks() {
        try {
            tasks.putAll(gson.fromJson(kvClient.load(TASKS.name()), HashMap.class));
            epics.putAll(gson.fromJson(kvClient.load(EPICS.name()), HashMap.class));
            subTasks.putAll(gson.fromJson(kvClient.load(SUBTASKS.name()), HashMap.class));
            subTasks.values().forEach(this::addSubTaskToEpic);
        } catch (IOException | InterruptedException e) {
            throw new HttpException(e.getMessage());
        }
    }

    private void restorePrioritizedTasks() {
        try {
            prioritizedList.addAll(gson.fromJson(kvClient.load(PRIORITIZED_LIST.name()), Set.class));
        } catch (IOException | InterruptedException e) {
            throw new HttpException(e.getMessage());
        }
    }

    private void restoreHistory() {
        ArrayList<Integer> restoreHistory = null;
        try {
            restoreHistory = gson.fromJson(kvClient.load(HISTORY.name()), ArrayList.class);
            restoreHistory.forEach(id -> historyManager.add(getTask(id)));
        } catch (IOException | InterruptedException e) {
            throw new HttpException(e.getMessage());
        }
    }

    @Override
    public void save()  {
        try {
            kvClient.put(TASKS.name(), gson.toJson(tasks));
            kvClient.put(EPICS.name(), gson.toJson(epics));
            kvClient.put(SUBTASKS.name(), gson.toJson(subTasks));
            kvClient.put(PRIORITIZED_LIST.name(), gson.toJson(prioritizedList));
            kvClient.put(HISTORY.name(), gson.toJson(historyManager.getHistory()));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
