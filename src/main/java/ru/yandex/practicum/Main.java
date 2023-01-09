package ru.yandex.practicum;

import com.google.gson.JsonParseException;
import ru.yandex.practicum.api.*;
import ru.yandex.practicum.managers.FileBackedTasksManager;
import ru.yandex.practicum.managers.HttpTaskManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Task task1 = new Task("Таск1", "-", ZonedDateTime.of(LocalDateTime.of(
                2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);
        Task task2 = new Task("Таск2", "-");
        Epic epic1 = new Epic("Эпик1", "-");
        Epic epic2 = new Epic("Эпик2", "-");
        SubTask subTask1 = new SubTask("Саб1", "-", 1, ZonedDateTime.of(LocalDateTime.of(
                2022, 2, 2, 0, 0), ZoneId.systemDefault()), 1);
        SubTask subTask2 = new SubTask("Саб2", "-", 1, ZonedDateTime.of(LocalDateTime.of(
                2022, 2, 2, 1, 0), ZoneId.systemDefault()), 1);
        SubTask subTask3 = new SubTask("Саб3", "-", 1);
        SubTask subTask4 = new SubTask("Саб4", "-", 1, ZonedDateTime.of(LocalDateTime.of(
                2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);


        TaskManager httpManager = Managers.getDefault();
        httpManager.create(epic1);
        httpManager.create(task1);
        httpManager.create(task2);
        httpManager.getTaskByID(task1.getID());
        httpManager.create(epic2);
        httpManager.create(subTask3);
        httpManager.create(subTask1);
        httpManager.create(subTask2);
        httpManager.deleteAllSubTasks();
        System.out.println(httpManager.getAllTasks());
        System.out.println(httpManager.getAllSubTasks());
        System.out.println(httpManager.getAllEpics());


//        TaskManager httpManager1 = Managers.getDefault();
//        System.out.println(httpManager1.getAllTasks());

//        System.out.println(httpManager.getAllTasks());


//        TaskManager manager = new FileBackedTasksManager(new File("src/main/resources/Backup.csv"));
//        Task task1 = new Task("Таск1", "-", ZonedDateTime.of(LocalDateTime.of(
//                2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);
//        Task task2 = new Task("Таск2", "-");
//        Epic epic1 = new Epic("Эпик1", "-");
//        Epic epic2 = new Epic("Эпик2", "-");
//        SubTask subTask1 = new SubTask("Саб1", "-", 1, ZonedDateTime.of(LocalDateTime.of(
//                2022, 2, 2, 0, 0), ZoneId.systemDefault()), 1);
//        SubTask subTask2 = new SubTask("Саб2", "-", 1, ZonedDateTime.of(LocalDateTime.of(
//                2022, 2, 2, 1, 0), ZoneId.systemDefault()), 1);
//        SubTask subTask3 = new SubTask("Саб3", "-", 1);
//        SubTask subTask4 = new SubTask("Саб4", "-", 1, ZonedDateTime.of(LocalDateTime.of(
//                2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);
//        manager.create(epic1);
//        manager.create(epic2);
//        manager.create(subTask3);
//        manager.create(subTask1);
//        manager.create(subTask2);
//        manager.create(task2);
//        manager.create(task1);
//        manager.getTaskByID(task2.getID());
//        manager.getEpicByID(epic2.getID());
//        manager.getEpicByID(epic1.getID());
//        manager.getSubTaskByID(subTask3.getID());




    }
}
