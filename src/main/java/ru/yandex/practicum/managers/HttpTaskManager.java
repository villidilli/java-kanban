package ru.yandex.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.api.APIException;
import ru.yandex.practicum.api.KVTaskClient;
import ru.yandex.practicum.tasks.*;

import com.google.gson.*;
import ru.yandex.practicum.utils.EpicToJsonConverter;
import ru.yandex.practicum.utils.SubtaskToJsonConverter;
import ru.yandex.practicum.utils.TaskToJsonConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;


import static ru.yandex.practicum.managers.HttpManagerKey.*;
import static ru.yandex.practicum.tasks.TaskTypes.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private static String kvServerURL;
    public final KVTaskClient kvClient; //todo заприватить
    private final Gson gson;

    public HttpTaskManager(String url) {
        try {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Task.class, new TaskToJsonConverter())
                    .registerTypeAdapter(SubTask.class, new SubtaskToJsonConverter())
                    .registerTypeAdapter(Epic.class, new EpicToJsonConverter())
                    .create();
            kvServerURL = url;
            kvClient = new KVTaskClient(url);
            load();
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }

    private void restoreID() {
        tasks.values().forEach(this::updateGeneratorID);
        subTasks.values().forEach(this::updateGeneratorID);
        epics.values().forEach(this::updateGeneratorID);
    }

    private void restoreTasks() {
        try {
            Type type;
            if (!kvClient.load(EPICS.name()).isEmpty()) {
                type = new TypeToken<ArrayList<Epic>>(){}.getType();
                ArrayList<Epic> epicList = gson.fromJson(kvClient.load(EPICS.name()), type);
                epicList.forEach(epic -> epics.put(epic.getID(), epic));
            }
            if (!kvClient.load(TASKS.name()).isEmpty()) {
                type = new TypeToken<ArrayList<Task>>(){}.getType();
                ArrayList<Task> taskList = (gson.fromJson(kvClient.load(TASKS.name()),type));
                taskList.forEach(task -> tasks.put(task.getID(), task));
            }
            if (!kvClient.load(SUBTASKS.name()).isEmpty()) {
                type = new TypeToken<ArrayList<SubTask>>(){}.getType();
                ArrayList<SubTask> subtaskList = gson.fromJson(kvClient.load(SUBTASKS.name()), type);
                subtaskList.forEach(subTask -> subTasks.put(subTask.getID(), subTask));
                subTasks.values().forEach(this::addSubTaskToEpic);
            }
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }

    private void restoreHistory() {
        try {
            if (!kvClient.load(HISTORY.name()).isEmpty()) {
                Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
                ArrayList<Integer> histIds = gson.fromJson(kvClient.load(HISTORY.name()), type);
                for (Integer id : histIds) {
                    if (tasks.get(id) != null) {
                        historyManager.add(tasks.get(id));
                    }
                    if (subTasks.get(id) != null) {
                        historyManager.add(subTasks.get(id));
                    }
                    if (epics.get(id) != null) {
                        historyManager.add(epics.get(id));
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }

    private void restorePrioritizedTasks() {
        prioritizedList.addAll(tasks.values());
        prioritizedList.addAll(subTasks.values());
    }

    private void load() { //todo Удалить ком после тестов (был паблик)
        restoreTasks();
        restoreHistory();
        restorePrioritizedTasks();
        restoreID();
        System.out.println("[HttpManager] восстановил данные с [KVServer]");
    }

    @Override
    public void save() { //todo был протект
        System.out.println("HttpManager save");
        try {
            kvClient.put(TASKS.name(), gson.toJson(getAllTasks()));
            kvClient.put(EPICS.name(), gson.toJson(getAllEpics()));
            kvClient.put(SUBTASKS.name(), gson.toJson(getAllSubTasks()));
            kvClient.put(PRIORITIZED_LIST.name(), gson.toJson(getPrioritizedTasks()));
            ArrayList<Integer> historyIds = new ArrayList<>();
            getHistory().stream().mapToInt(Task::getID).forEach(historyIds::add);
            kvClient.put(HISTORY.name(), gson.toJson(historyIds));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}





