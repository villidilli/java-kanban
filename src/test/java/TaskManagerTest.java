import org.junit.jupiter.api.*;

import ru.yandex.practicum.managers.FileBackedTasksManager;
import ru.yandex.practicum.managers.InMemoryTaskManager;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.*;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.tasks.Task.UNREACHEBLE_DATE;


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
        epic1 = new Epic("Эпик1", "-");
//        backedManager.create(epic1); //1
        subTask1 = new SubTask("Саб1", "-",1,2023, 1, 1, 0, 5, 5);
//        backedManager.create(subTask1); //2
        task1 = new Task("Таск1", "-");
//        backedManager.create(task1); //3
        task2 = new Task("Таск2", "-", 2022, 1, 1, 0, 5, 5);
//        backedManager.create(task2); //4
        task3 = new Task("Таск3", "-", 2024, 1, 1, 23, 58, 1);
//        backedManager.create(task3); //5
        subTask2 = new SubTask("Саб2", "-", 1, 15000, 1, 1, 0, 5, 5); //6
//        backedManager.create(subTask2);
//
    }
//
//    @Test
//    public void createTest() {
//        Task expectedTask = task1;
//        SubTask expectedSubTask = subTask1;
//        Epic expectedEpic = epic1;
//
//        assertEquals(expectedTask, backedManager.getTaskByID(3));
//        assertEquals(expectedSubTask, backedManager.getSubTaskByID(2));
//        assertEquals(expectedEpic, backedManager.getEpicByID(1));
//    }
//
//    @Test
//    public void updateTest() {
//        String newName = "Новый Эпик1";
//        Status newStatus = Status.IN_PROGRESS;
//        String newDescription = "Новое описание";
//
//        epic1.setName(newName);
//        backedManager.update(epic1);
//        subTask1.setStatus(Status.IN_PROGRESS);
//        backedManager.update(subTask1);
//        task1.setDescription(newDescription);
//        backedManager.update(task1);
//
//
//        assertEquals(newName, backedManager.getEpicByID(1).getName());
//        assertEquals(newStatus, backedManager.getSubTaskByID(2).getStatus());
//        assertEquals(newDescription, backedManager.getTaskByID(3).getDescription());
//    }
//
//    @Test
//    public void getAllTypeTasksTest() {
//        Task[] expectedArrayOfTasks = {task1, task2, task3};
//        SubTask[] expectedArrayOfSubTasks = {subTask1, subTask2};
//        Epic[] expectedArrayOfEpics = {epic1};
//
//        assertArrayEquals(expectedArrayOfTasks, backedManager.getAllTasks().toArray());
//        assertArrayEquals(expectedArrayOfSubTasks, backedManager.getAllSubTasks().toArray());
//        assertArrayEquals(expectedArrayOfEpics, backedManager.getAllEpics().toArray());
//    }
//
//    @Test
//    public void deleteAllTasksTest() {
//        int expectedTasksListSize = 0;
//        backedManager.deleteAllTasks();
//        assertEquals(expectedTasksListSize, backedManager.getAllTasks().size());
//        assertTrue(backedManager.deleteAllTasks());
//
//
//    }

    @Test
    public void deleteAllSubTasksTest() {
//        backedManager = new FileBackedTasksManager(new File("src/main/resources/Backup.csv"));
//
//        epic1 = new Epic("Эпик1", "-");
//        backedManager.create(epic1); //1
//        subTask1 = new SubTask("Саб1", "-",epic1.getID()); // тут инициализировалось поле датой с годом 9999
//        backedManager.create(subTask1); //2
//        task1 = new Task("Таск1", "-");
//        backedManager.create(task1); //3
//        System.out.println("ДО ОБНОВЛЕНИЯ");
//        System.out.println(backedManager.getPrioritizedTasks());
//        subTask1 = new SubTask(subTask1.getID(), subTask1.getName(), subTask1.getDescription(), subTask1.getParentEpicID());
//        subTask1.setStartTime(ZonedDateTime.of(
//                10000,1,1,0,0,0,0,ZoneId.systemDefault()));
//
//        backedManager.update(subTask1);
//        System.out.println("ПОСЛЕ ОБНОВЛЕНИЯ");
//        System.out.println(backedManager.getPrioritizedTasks());
//
//        subTask1 = new SubTask(subTask1.getID(), subTask1.getName(), subTask1.getDescription(), subTask1.getParentEpicID());
//        subTask1.setStartTime(ZonedDateTime.of(
//                5000,1,1,0,0,0,0,ZoneId.systemDefault()));
//
//        backedManager.update(subTask1);
//        System.out.println("ПОСЛЕ ОБНОВЛЕНИЯ2");
//        System.out.println(backedManager.getPrioritizedTasks());
        backedManager.create(epic1);
        backedManager.create(task1);
        backedManager.create(task2);
        backedManager.create(task3);
        backedManager.create(subTask1);
        backedManager.create(subTask2);

        System.out.println(backedManager.getPrioritizedTasks());
    }

}

