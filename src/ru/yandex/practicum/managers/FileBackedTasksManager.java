package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.utils.TaskConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {
        //имитируем созданные ранее задачи
        FileBackedTasksManager f = new FileBackedTasksManager();

        Task task1 = new Task("Таск1", "-"); //1
        f.create(task1);
        Epic epic1 = new Epic("Эпик1", "-"); //2
        f.create(epic1);
        SubTask subTask1 = new SubTask("Саб1", "-", 2); //3
        f.create(subTask1);
        SubTask subTask2 = new SubTask("Саб2", "-", 2); //4
        f.create(subTask2);
        SubTask subTask3 = new SubTask("Саб3", "-", 2); //5
        f.create(subTask3);

        f.getSubTaskByID(subTask2.getID());
        f.getSubTaskByID(subTask3.getID());
        f.getSubTaskByID(subTask1.getID());
        f.getEpicByID(epic1.getID());


        System.out.println("\nПроверяем сохранение истории после создания нового менеджера");
        //создаем новый менеджер через статический метод
        FileBackedTasksManager f1 = loadFromFile(backupFile.toFile());
        System.out.println("\nВыводим все Задачи");
        System.out.println(f1.getAllTasks());
        System.out.println("\nВыводим все Подзадачи");
        System.out.println(f1.getAllSubTasks());
        System.out.println("\nВыводим все Эпики");
        System.out.println(f1.getAllEpics());
        System.out.println("\nПроверяем историю (без дублей)");
        System.out.println("\nОжидаем порядок id 4 - 5 - 3 - 2");
        System.out.println(f1.getHistory());
    }

    private static final Path backupFile = Path.of("resources\\Backup.csv");

    //заменить на класс
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager backedTasksManager = new FileBackedTasksManager();
        HistoryManager historyManager = backedTasksManager.historyManager;
        //считали из файла все строки
        String[] rows = readFile(file);
        //начинаем с 1 чтобы пропустить шапку
        for (int i = 1; i < rows.length; i++) {
            //если строка пустая, значит следующая строка истории
            if (rows[i].isBlank()) {
                //прибавили к индексу 1, чтобы получить следующую строку (историю)
                //сконвертировали строку истории в список ID
                List<Integer> history = TaskConverter.historyFromString(rows[++i]);
                Task task = null;
                //ищет задачу в мапах по ID и записываем в историю
                for (Integer ID : history) {
                    if (backedTasksManager.tasks.containsKey(ID)) {
                        task = backedTasksManager.tasks.get(ID);
                    } else if (backedTasksManager.subTasks.containsKey(ID)) {
                        task = backedTasksManager.subTasks.get(ID);
                    } else if (backedTasksManager.epics.containsKey(ID)) {
                        task = backedTasksManager.epics.get(ID);
                    }
                    historyManager.add(task);
                }
                //если строка не пустая значит мы работаем со строками-задачами
            } else {
                Task task = TaskConverter.taskFromString(rows[i]);
                switch (task.getTaskType()) {
                    case TASK:
                        backedTasksManager.tasks.put(task.getID(), task);
                        break;
                    case SUBTASK:
                        backedTasksManager.subTasks.put(task.getID(), (SubTask) task);
                        break;
                    case EPIC:
                        backedTasksManager.epics.put(task.getID(), (Epic) task);
                }
            }
        }
        return backedTasksManager;
    }

    private static String[] readFile(File file) {
        String fileData;
        String[] rows;
        //вначале считали файл целиком в String, потом рассплитили по строкам в массив
        try {
            fileData = Files.readString(file.toPath());
            rows = fileData.split("\\r?\\n");
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Чтение файла невозможно");
        }
        return rows;
    }

    private void writeHistoryToFile(BufferedWriter bufferedWriter, HistoryManager historyManager) {
        //получили историю (ID) в строчном представлении
        String history = TaskConverter.historyToString(historyManager);
        try {
            bufferedWriter.write(history);
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Не удалось записать в файл историю");
        }
    }

    private void writeTaskToFile(BufferedWriter bufferedWriter, Map<Integer, ? extends Task> list) {
        //принимаем одну из мап с задачами и перебираем значения (объекты)
        try {
            for (Task task : list.values()) {
                //пишем в файл полученную в строчном представлении задачу
                bufferedWriter.write(TaskConverter.taskToString(task) + "\n");
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Не удалось записать данные задачи");
        }
    }

    private String getHeaderTasks() {
        return "id,type,name,status,description,epic\n";
    }

    private void save() {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(backupFile.toFile(), StandardCharsets.UTF_8))) {
            //записали шапку
            bufferedWriter.write(getHeaderTasks());
            writeTaskToFile(bufferedWriter, tasks);
            writeTaskToFile(bufferedWriter, subTasks);
            writeTaskToFile(bufferedWriter, epics);
            bufferedWriter.newLine();
            writeHistoryToFile(bufferedWriter, historyManager);
            bufferedWriter.flush();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка -> Не удалось записать объекты в файл");
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