import org.junit.jupiter.api.*;

import ru.yandex.practicum.managers.FileBackedTasksManager;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.*;

import java.io.File;


public abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager backedManager;
    Task SubTask1;
    Task SubTask2;
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
        SubTask1 = new SubTask("Таск1", "-", 1,2022, 1, 1, 0, 0, 1);
        backedManager.create(SubTask1);

        SubTask2 = new SubTask("Таск2", "-", 1, 2022, 1, 1, 1, 0, 1);
        backedManager.create(SubTask2);

        System.out.println(backedManager.getPrioritizedTasks());
        System.out.println("ПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ БЕЗ ПЕРЕСЕЧЕНИЯ");
        SubTask1 = new SubTask(
                SubTask1.getID(),
                SubTask1.getName(),
                SubTask1.getDescription(),1,
                2022, 1, 1, 2, 0,1);

        backedManager.update(SubTask1);

        System.out.println(backedManager.getPrioritizedTasks());

        System.out.println("ПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ КЛЮЧЕЙ");
        SubTask2 = new SubTask(
                SubTask2.getID(),
                SubTask2.getName(),
                SubTask2.getDescription(), 1,
                2022, 1, 1, 2, 0,3);
        System.out.println(SubTask2.getName() + " Интервал: " + SubTask2.getStartTime().toLocalTime() + " - " + SubTask2.getEndTime().toLocalTime());
        backedManager.update(SubTask2);

        System.out.println(backedManager.getPrioritizedTasks());

        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ ИНТЕРВАЛОВ");
        SubTask2 = new SubTask(
                SubTask2.getID(),
                SubTask2.getName(),
                SubTask2.getDescription(), 1,
                2022, 1, 1, 1, 59,3);
        System.out.println(SubTask2.getName() + " Интервал: " + SubTask2.getStartTime().toLocalTime() + " - " + SubTask2.getEndTime().toLocalTime());
        backedManager.update(SubTask2);
        System.out.println(backedManager.getPrioritizedTasks());

        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ БЕЗ ПЕРЕСЕЧЕНИЯ");
        SubTask2 = new SubTask(
                SubTask2.getID(),
                SubTask2.getName(),
                SubTask2.getDescription(), 1,
                2022, 1, 1, 3, 0,1);
        System.out.println(SubTask2.getName() + " Интервал: " + SubTask2.getStartTime().toLocalTime() + " - " + SubTask2.getEndTime().toLocalTime());
        backedManager.update(SubTask2);
        System.out.println(backedManager.getPrioritizedTasks());

        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ");
        SubTask2 = new SubTask(
                SubTask2.getID(),
                SubTask2.getName(),
                SubTask2.getDescription(), 1,
                2022, 1, 1, 1, 59,1);
        System.out.println(SubTask2.getName() + " Интервал: " + SubTask2.getStartTime().toLocalTime() + " - " + SubTask2.getEndTime().toLocalTime());
        backedManager.update(SubTask2);
        System.out.println(backedManager.getPrioritizedTasks());

        task3 = new Task("Таск3", "-");
        backedManager.create(task3);
        System.out.println(backedManager.getPrioritizedTasks());
        task3 = new Task(
                task3.getID(),
                task3.getName(),
                task3.getDescription(),
                2022, 1, 1, 2,30,1);
        backedManager.update(task3);
        System.out.println(backedManager.getPrioritizedTasks());
    }

}

