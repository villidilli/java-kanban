package ru.yandex.practicum.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ru.yandex.practicum.api.APIException;
import ru.yandex.practicum.api.KVTaskClient;
import ru.yandex.practicum.tasks.*;

import ru.yandex.practicum.utils.GsonConfig;

import java.io.IOException;

import java.lang.reflect.Type;

import java.util.*;

import static ru.yandex.practicum.managers.HttpManagerKey.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvClient;
    private final Gson gson;

    public HttpTaskManager(String url) {
        try {
            gson = GsonConfig.getGsonTaskConfig();
            kvClient = new KVTaskClient(url);
            load();
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }

    private void load() { //todo Удалить ком после тестов (был паблик)
        restoreTasks();
        restoreHistory();
        restorePrioritizedTasks();
        restoreID();
        System.out.println("[HttpManager] восстановил данные с [KVServer]");
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
                type = new TypeToken<ArrayList<Epic>>() {
                }.getType();
                ArrayList<Epic> epicList = gson.fromJson(kvClient.load(EPICS.name()), type);
                epicList.forEach(epic -> epics.put(epic.getID(), epic));
            }
            if (!kvClient.load(TASKS.name()).isEmpty()) {
                type = new TypeToken<ArrayList<Task>>() {
                }.getType();
                ArrayList<Task> taskList = (gson.fromJson(kvClient.load(TASKS.name()), type));
                taskList.forEach(task -> tasks.put(task.getID(), task));
            }
            if (!kvClient.load(SUBTASKS.name()).isEmpty()) {
                type = new TypeToken<ArrayList<SubTask>>() {
                }.getType();
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
                Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
                ArrayList<Integer> histIds = gson.fromJson(kvClient.load(HISTORY.name()), type);
                addToHistory(histIds);
            }
        } catch (IOException | InterruptedException e) {
            throw new APIException(e.getMessage());
        }
    }

    private void addToHistory(ArrayList<Integer> historyIDs) {
        for (Integer id : historyIDs) {
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

    private void restorePrioritizedTasks() {
        prioritizedList.addAll(tasks.values());
        prioritizedList.addAll(subTasks.values());
    }

    @Override
    protected void save() { //todo был протект
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