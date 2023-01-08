package ru.yandex.practicum;

import com.google.gson.JsonParseException;
import ru.yandex.practicum.api.HttpException;
import ru.yandex.practicum.api.HttpTaskServer;
import ru.yandex.practicum.api.KVTaskClient;
import ru.yandex.practicum.api.Servers;
import ru.yandex.practicum.managers.FileBackedTasksManager;
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

//        Servers.getKVServer().start();
//        KVTaskClient client1 = new KVTaskClient("http://localhost:8078");
//        String token1 = client1.getAPI_TOKEN();
//        KVTaskClient client2 = new KVTaskClient("http://localhost:8078");
//        String token2 = client2.getAPI_TOKEN();
//        System.out.println("client 1 TOKEN" + client1.getAPI_TOKEN());
//        System.out.println("client 2 TOKEN" + client2.getAPI_TOKEN());
//
//        String body1 = "{\n" +
//                "\t\"name\": \"task2\",\n" +
//                "\t\"description\": \"-\",\n" +
//                "\t\"startDateTime\": \"01-01-2022 | 03:00 | +03:00\",\n" +
//                "\t\"duration\": 1\n" +
//                "}";
//        String body2 = "{\n" +
//                "\t\"name\": \"task2\",\n" +
//                "\t\"description\": \"-\",\n" +
//                "\t\"startDateTime\": \"01-01-2022 | 23:59 | +03:00\",\n" +
//                "\t\"duration\": 1\n" +
//                "}";

//                try {
//                    client1.put(client1.getAPI_TOKEN(), body1);
//                    System.out.println("LOAD" + client1.load(client1.getAPI_TOKEN()));
//                    client1.put(client1.getAPI_TOKEN(), body2);
//                    System.out.println("LOAD" + client1.load(client1.getAPI_TOKEN()));
//                } catch (IOException | InterruptedException | HttpException | JsonParseException e) {
//                    System.out.println(e.getMessage());
//                }




        try {
//            Servers.getHttpTaskServer().start();
            Servers.getKVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
