package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.utils.TaskConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File backupfile; //todo заменить на статик, что будет

    public FileBackedTasksManager() {}

    public FileBackedTasksManager(File file) {
        backupfile = file;
    }

    public static FileBackedTasksManager load(File file) {
        final FileBackedTasksManager backedManager = new FileBackedTasksManager(file);
        final HistoryManager histManager = backedManager.historyManager;
        //считали из файла все строки
        String[] rows = readFile(file);
        //начинаем с 1 чтобы пропустить шапку
        List<Integer> history = Collections.emptyList();
        for (int i = 1; i < rows.length; i++) {
            //если строка пустая, значит следующая строка истории
            String line = rows[i];
            Task task;
            if (line.isBlank()) {
                //прибавили к индексу 1, чтобы получить следующую строку (историю)
                //сконвертировали строку истории в список ID
                line = rows[i + 1];
                history = TaskConverter.historyFromString(line);
                break;
            }
            //если строка не пустая значит мы работаем со строками-задачами
            task = TaskConverter.taskFromString(line);
            backedManager.updateGeneratorID(task);
            backedManager.addTask(task);
        }
        //ищет задачу в мапах по ID и записываем в историю
        history.forEach(ID -> histManager.add(backedManager.getTask(ID)));
        return backedManager;
    }

    protected static String[] readFile(File file) {
        String fileData;
        String[] rows;
        //вначале считали файл целиком в String, потом рассплитили по строкам в массив
        try {
            fileData = Files.readString(file.toPath());
            rows = fileData.split(TaskConverter.LINE_SEPARATOR);
        } catch (IOException exception) {
            throw new ManagerSaveException(ManagerSaveException.NOT_READ_FILE + file.getName().toUpperCase());
        }
        return rows;
    }

    private void addTask(Task task) {
        if (task == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        int ID = task.getID();
        switch (task.getType()) {
            case TASK:
                tasks.put(ID, task);
                break;
            case SUBTASK:
                subTasks.put(ID, (SubTask) task);
                addSubTaskToEpic((SubTask) task);
                break;
            case EPIC:
                epics.put(ID, (Epic) task);
        }
    }

    private void addSubTaskToEpic(SubTask subTask) {
        if (subTask == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        Epic epic = (Epic) getTask(subTask.getParentEpicID());
        epic.getEpicSubTasks().put(subTask.getID(), subTask);
    }

    private Task getTask(int ID) {
        Task task = tasks.get(ID);
        if (task == null) {
            task = subTasks.get(ID);
        }
        if (task == null) {
            task = epics.get(ID);
        }
        return task;
    }

    private void updateGeneratorID(Task task) {
        if (task == null) {
            throw new ManagerNotFoundException(ManagerNotFoundException.NOT_TRANSFERRED_INPUT);
        }
        if (generatorID <= task.getID()) {
            generatorID = task.getID() + 1;
        }
    }

    private void writeHistoryToFile(BufferedWriter bufferedWriter, HistoryManager historyManager) {
        //получили историю (ID) в строчном представлении
        String history = TaskConverter.historyToString(historyManager);
        try {
            bufferedWriter.write(history);
        } catch (IOException exception) {
            throw new ManagerSaveException(ManagerSaveException.NOT_WRITE_FILE + backupfile.getName().toUpperCase());
        }
    }

    private void writeTaskToFile(BufferedWriter bufferedWriter, Map<Integer, ? extends Task> list) {
        //принимаем одну из мап с задачами и перебираем значения (объекты)
        list.values().forEach(task -> {
            try {
                //пишем в файл полученную в строчном представлении задачу
                bufferedWriter.write(TaskConverter.taskToString(task));
                bufferedWriter.newLine();
            } catch (IOException exception) {
                throw new ManagerSaveException(ManagerSaveException.NOT_WRITE_FILE + backupfile.getName().toUpperCase());
            }
        });
    }

    private String getHeaderTasks() {
        return "id,type,name,status,description,epic,start,duration";
    }

    protected void save() {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(backupfile, StandardCharsets.UTF_8))) {
            //записали шапку
            bufferedWriter.write(getHeaderTasks());
            bufferedWriter.newLine();
            writeTaskToFile(bufferedWriter, tasks);
            writeTaskToFile(bufferedWriter, epics);
            writeTaskToFile(bufferedWriter, subTasks);
            bufferedWriter.newLine();
            writeHistoryToFile(bufferedWriter, historyManager);
            bufferedWriter.flush();
        } catch (IOException exception) {
            throw new ManagerSaveException(ManagerSaveException.NOT_SAVE + backupfile.getName().toUpperCase());
        }
    }

    @Override
    public void create(Task newTask) {
        super.create(newTask);
        save();
    }

    @Override
    public void create(SubTask newSubTask) {
        super.create(newSubTask);
        save();
    }

    @Override
    public void create(Epic newEpic) {
        super.create(newEpic);
        save();
    }

    @Override
    public void update(Task newTask) {
        super.update(newTask);
        save();
    }

    @Override
    public void update(SubTask newSubTask) {
        super.update(newSubTask);
        save();
    }

    @Override
    public void update(Epic newEpic) {
        super.update(newEpic);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Task getTaskByID(int ID) {
        Task task = super.getTaskByID(ID);
        save();
        return task;
    }

    @Override
    public SubTask getSubTaskByID(int ID) {
        SubTask subTask = super.getSubTaskByID(ID);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicByID(int ID) {
        Epic epic = super.getEpicByID(ID);
        save();
        return epic;
    }

    @Override
    public void deleteTaskByID(int ID) {
        super.deleteTaskByID(ID);
        save();
    }

    @Override
    public void deleteSubTaskByID(int ID) {
        super.deleteSubTaskByID(ID);
        save();
    }

    @Override
    public void deleteEpicByID(int ID) {
        super.deleteEpicByID(ID);
        save();
    }
}