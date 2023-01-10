package ru.yandex.practicum;

import ru.yandex.practicum.api.*;
import ru.yandex.practicum.managers.HttpTaskManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static ru.yandex.practicum.api.RequestMethod.GET;


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
        Servers.getHttpTaskServer().start();
        HttpTaskManager manager = Managers.getDefault();
        manager.create(epic1);
        manager.create(task1);
        manager.create(task2);
        manager.create(subTask1);
        manager.getTaskByID(task1.getID());
        manager.getSubTaskByID(subTask1.getID());
        manager.getTaskByID(task2.getID());
        System.out.println(manager.getPrioritizedTasks());

//        HttpTaskManager manager1 = new HttpTaskManager("http://localhost:8078");

        TasksHandler tasksHandler = new TasksHandler(manager);
        System.out.println(tasksHandler.getEndpoint(URI.create("http://localhost:8078/tasks"), GET));


    }

    private static HttpResponse<String> getResponse(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/" + path))
                .headers("Content-Type", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
