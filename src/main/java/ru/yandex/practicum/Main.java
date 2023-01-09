package ru.yandex.practicum;


import ru.yandex.practicum.api.*;
import ru.yandex.practicum.managers.HttpTaskManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;


import java.io.File;
import java.io.IOException;
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


        Servers.getKVServer().start();
        TaskManager manager = Managers.getDefault();
        manager.create(task1);
        System.out.println(manager.getAllTasks());

        HttpTaskManager manager1 = new HttpTaskManager("http://localhost:8078");

    }
}
