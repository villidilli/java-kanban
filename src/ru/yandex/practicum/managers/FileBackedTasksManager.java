package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TaskTypes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path path = Paths.get("D:\\JavaDev\\java-kanban\\resources\\Backup.csv");

    private static void loadFromFile(File file) {
    }

    //заменить на private
    public String toString(Task task) {
        StringBuilder stringBuilder = new StringBuilder();

        // добавляем поля, которые не привязываны к типу класса
        stringBuilder.append(task.getID() + ",");
        stringBuilder.append(task.getName() + ",");
        stringBuilder.append(task.getStatus() + ",");
        stringBuilder.append(task.getDescription() + ",");

        // добавляем название класса = константе enum
        if (task.getClass() == Task.class) {
            stringBuilder.insert(2, TaskTypes.TASK + ",");
        } else if (task.getClass() == SubTask.class) {
            stringBuilder.insert(2, TaskTypes.SUBTASK + ",");
            stringBuilder.append(((SubTask) task).getParentEpicID());
        } else if (task.getClass() == Epic.class) {
            stringBuilder.insert(2, TaskTypes.EPIC + ",");
        } else {
            throw new ManagerSaveException("Ошибка -> Неизвестный тип класса");
        }
        return stringBuilder.toString();
    }

    private void writeFile(FileWriter fileWriter, List<? extends Task> list) {
        for (Task task : list) {
            try {
                fileWriter.write(toString(task) + "\n");
            } catch (IOException exception) {
                throw new ManagerSaveException("Ошибка -> Не удалось записать данные");
            }
        }
    }

    //сделать Private
    public void save() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path.toFile(), StandardCharsets.UTF_8);
            writeFile(fileWriter, super.getAllTasks());
            writeFile(fileWriter, super.getAllSubTasks());
            writeFile(fileWriter, super.getAllEpics());
            fileWriter.close();
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
