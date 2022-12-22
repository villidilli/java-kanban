import org.junit.jupiter.api.*;

import ru.yandex.practicum.managers.FileBackedTasksManager;
import ru.yandex.practicum.managers.InMemoryTaskManager;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.utils.TimeConverter;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager backedManager;
    Task task1;
    Task task2;
    Task task3;
    Epic epic1;
    SubTask subTask1;
    SubTask subTask2;


    @BeforeEach
    public void beforeEach() {
        backedManager = new FileBackedTasksManager(new File("src/main/resources/Backup.csv"));
    }

    @Test
    public void deleteAllSubTasksTest() {

        System.out.println("ПЕРВОЕ ДОБАВЛЕНИЕ");
        task1 = new Task("Таск1", "-", 2022, 1, 1, 0, 0, 1);
        backedManager.create(task1);

        task2 = new Task("Таск2", "-", 2022, 1, 1, 1, 0, 1);
        backedManager.create(task2);

        System.out.println(backedManager.getPrioritizedTasks());
        System.out.println("ПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ БЕЗ ПЕРЕСЕЧЕНИЯ");
        task1 = new Task(
                task1.getID(),
                task1.getName(),
                task1.getDescription(),
                2022, 1, 1, 2, 0,1);

        backedManager.update(task1);

        System.out.println(backedManager.getPrioritizedTasks());

        System.out.println("ПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ КЛЮЧЕЙ");
        task2 = new Task(
                task2.getID(),
                task2.getName(),
                task2.getDescription(),
                2022, 1, 1, 2, 0,1);

        backedManager.update(task2);

        System.out.println(backedManager.getPrioritizedTasks());

        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ ИНТЕРВАЛОВ");
        task2 = new Task(
                task2.getID(),
                task2.getName(),
                task2.getDescription(),
                2022, 1, 1, 1, 59,3);
        System.out.println(task2.getName() + " Интервал: " + task2.getStartTime().toLocalTime() + " - " + task2.getEndTime().toLocalTime());
        backedManager.update(task2);
        System.out.println(backedManager.getPrioritizedTasks());

        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ БЕЗ ПЕРЕСЕЧЕНИЯ");
        task2 = new Task(
                task2.getID(),
                task2.getName(),
                task2.getDescription(),
                2022, 1, 1, 3, 0,1);
        System.out.println(task2.getName() + " Интервал: " + task2.getStartTime().toLocalTime() + " - " + task2.getEndTime().toLocalTime());
        backedManager.update(task2);
        System.out.println(backedManager.getPrioritizedTasks());

        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ");
        task2 = new Task(
                task2.getID(),
                task2.getName(),
                task2.getDescription(),
                2022, 1, 1, 1, 59,1);
        System.out.println(task2.getName() + " Интервал: " + task2.getStartTime().toLocalTime() + " - " + task2.getEndTime().toLocalTime());
        backedManager.update(task2);
        System.out.println(backedManager.getPrioritizedTasks());
    }

}

