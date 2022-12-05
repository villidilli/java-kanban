package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.utilities.TaskConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) {
        FileBackedTasksManager f = new FileBackedTasksManager();

        Task task1 = new Task("Таск1", "-"); //1
        f.create(task1);
        Epic epic1 = new Epic("Эпик1", "-"); //2
        f.create(epic1);
        SubTask subTask1 = new SubTask("Саб1", "-", 2); //3
        f.create(subTask1);
        SubTask subTask2 = new SubTask("Саб2", "-", 2); //4
        f.create(subTask2);
        SubTask subTask3 = new SubTask("Саб3", "-",2); //5
        f.create(subTask3);

        System.out.println("\nПроверяем порядок истории (без дублей)");
        f.getSubTaskByID(subTask2.getID());
        f.getSubTaskByID(subTask3.getID());
        f.getEpicByID(epic1.getID());
        System.out.println("\nОжидаем порядок id 4 - 5 - 2");
        System.out.println(f.getHistory());

        FileBackedTasksManager f1 = loadFromFile(backupFile.toFile());
        System.out.println(f1.tasks);

    }

    private static final Path backupFile = Path.of("resources\\Backup.csv");

    //заменить на класс
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fbtm = new FileBackedTasksManager();
        boolean isWasBlankLine = false;
        String[] rows = readFile(file);

        if (!isWasBlankLine) {
            int numBlankRow = 0;
            for (String row : rows) {
                if (!row.isBlank()) {
                    addTaskToMap(fbtm, TaskConverter.taskFromString(row));
                } else {
                    isWasBlankLine = true;
                    n
                    break;
                }
            }
        } else if (isWasBlankLine){

        }

        for (int numRow = 1; numRow < rows.length ; numRow++) { //TODO заходит в строку истории и падает 7 эл
            if(!rows[numRow].isBlank()) {
                addTaskToMap(fbtm,TaskConverter.taskFromString(rows[numRow]));
            } //TODO историю
        }
        return new FileBackedTasksManager();
    }

    private static void addTaskToMap(FileBackedTasksManager fbtm, Task task) {
        if (task.getClass() == Task.class) {
            fbtm.tasks.put(task.getID(), task);
        } else if (task.getClass() == SubTask.class) {
            fbtm.subTasks.put(task.getID(), (SubTask) task);
        } else if (task.getClass() == Epic.class) {
            fbtm.epics.put(task.getID(), (Epic) task);
        }
    }

//    private static void addHistoryToMap(FileBackedTasksManager fbtm,)

    private static String[] readFile(File file) {
        String fileData;
        String[] rows;
        try {
            fileData = Files.readString(file.toPath());
            rows = fileData.split("\\r?\\n");
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Чтение файла невозможно");
        }
        return rows;
    }


    private void writeHistoryToFile(BufferedWriter bufferedWriter, HistoryManager historyManager) {
        String history = TaskConverter.historyToString(historyManager);
        try {
            bufferedWriter.write(history);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Не удалось записать данные");
        }
    }

    private void writeTaskToFile(BufferedWriter bufferedWriter, Map<Integer, ? extends Task> list) {
        try {
            for (Task task : list.values()) {
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
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(backupFile.toFile(),
                                                                StandardCharsets.UTF_8))) {
            bufferedWriter.write(getHeaderTasks());
            writeTaskToFile(bufferedWriter, tasks);
            writeTaskToFile(bufferedWriter, subTasks);
            writeTaskToFile(bufferedWriter, epics);
            bufferedWriter.newLine();
            writeHistoryToFile(bufferedWriter, historyManager);
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
