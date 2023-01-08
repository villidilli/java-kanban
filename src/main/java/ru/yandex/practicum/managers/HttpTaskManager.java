package ru.yandex.practicum.managers;

import com.google.gson.Gson;
import ru.yandex.practicum.api.APIException;
import ru.yandex.practicum.api.HttpTaskServer;
import ru.yandex.practicum.api.KVTaskClient;
import ru.yandex.practicum.tasks.*;

import com.google.gson.*;
import ru.yandex.practicum.utils.EpicToJsonConverter;
import ru.yandex.practicum.utils.SubtaskToJsonConverter;
import ru.yandex.practicum.utils.TaskToJsonConverter;

import java.io.IOException;
import java.util.*;


import static ru.yandex.practicum.managers.HttpManagerKey.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private static String serverURL;
    private final KVTaskClient kvClient;
    private final Gson gson;
    private HttpTaskServer httpServer;

//    private final FileBackedTasksManager backedManager;

    public HttpTaskManager(String url) {
        try {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Task.class, new TaskToJsonConverter())
                    .registerTypeAdapter(SubTask.class, new SubtaskToJsonConverter())
                    .registerTypeAdapter(Epic.class, new EpicToJsonConverter())
                    .create();
//            backedManager = new FileBackedTasksManager();
            httpServer = new HttpTaskServer();
            serverURL = url;
            kvClient = new KVTaskClient(url);
            load();
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }

    private void load() {
        restoreTasks();
        restoreHistory();
        restorePrioritizedTasks();
        restoreID();
    }

    @Override
    protected void save() {
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

    private void restoreID() {
        tasks.values().forEach(this::updateGeneratorID);
        subTasks.values().forEach(this::updateGeneratorID);
        epics.values().forEach(this::updateGeneratorID);
    }

    private void restoreTasks() {
        try {
            if (!kvClient.load(EPICS.name()).isBlank()) {
                epics.putAll(gson.fromJson(kvClient.load(TASKS.name()), HashMap.class));
            }

            if (!kvClient.load(TASKS.name()).isBlank()) {
                tasks.putAll(gson.fromJson(kvClient.load(TASKS.name()), HashMap.class));
            }

            if (!kvClient.load(SUBTASKS.name()).isBlank()) {
                subTasks.putAll(gson.fromJson(kvClient.load(TASKS.name()), HashMap.class));
                subTasks.values().forEach(this::addSubTaskToEpic);
            }
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }

    private void restoreHistory() {
        ArrayList<Integer> restoreHistory = null;
        try {
            if (!kvClient.load(HISTORY.name()).isBlank()) {
                restoreHistory = gson.fromJson(kvClient.load(HISTORY.name()), ArrayList.class);
                restoreHistory.forEach(id -> historyManager.add(getTask(id)));
            }
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }

    private void restorePrioritizedTasks() {
        try {
            if (!kvClient.load(PRIORITIZED_LIST.name()).isBlank()) {
                prioritizedList.addAll(gson.fromJson(kvClient.load(PRIORITIZED_LIST.name()), Set.class));
            }
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }
}





