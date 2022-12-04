package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.utilities.TaskConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) {
        loadFromFile(FileBackedTasksManager.backupFile);
    }

    private static final File backupFile = new File("resources\\Backup.csv");
    //заменить на класс
    private static String loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.readFile(file);

        return " ";
    }

    private void readFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(
                FileBackedTasksManager.backupFile, StandardCharsets.UTF_8))) {


        } catch (FileNotFoundException exception) {
            throw new ManagerSaveException("Ошибка -> Файл не найден");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка -> Ошибка чтения файла");
        }
    }

    private void writeHistoryToFile(BufferedWriter bufferedWriter, List<? extends Task> list) {
        String history = TaskConverter.historyToString(list);
        try {
            bufferedWriter.write(history);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Не удалось записать данные");
        }
    }

    private void writeTaskToFile(BufferedWriter bufferedWriter, List<? extends Task> list) {
        try {
            for (Task task : list) {
                bufferedWriter.write(TaskConverter.taskToString(task) + "\n");
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Не удалось записать данные");
        }
    }

    private String getHeaderTasks() {
        return "id,type,name,status,description,epic\n";
    }

    //сделать Private
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FileBackedTasksManager.backupFile, StandardCharsets.UTF_8))) {
            bufferedWriter.write(getHeaderTasks());
            writeTaskToFile(bufferedWriter, super.getAllTasks());
            writeTaskToFile(bufferedWriter, super.getAllSubTasks());
            writeTaskToFile(bufferedWriter, super.getAllEpics());
            bufferedWriter.newLine();
            writeHistoryToFile(bufferedWriter, super.getHistory());
            bufferedWriter.flush();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Сохранение не удалось");
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
