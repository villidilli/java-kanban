import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.yandex.practicum.managers.FileBackedTasksManager;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.*;

import java.io.File;


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
        backedManager.create(epic1); //1
        subTask1 = new SubTask("Саб1", "-",epic1.getID());
        backedManager.create(subTask1); //2
        task1 = new Task("Таск1", "-");
        backedManager.create(task1); //3
        task2 = new Task("Таск2", "-");
        backedManager.create(task2); //4
        task3 = new Task("Таск3", "-");
        backedManager.create(task3); //5
        subTask2 = new SubTask("Саб2", "-", epic1.getID()); //6
        backedManager.create(subTask2);

    }

    @Test
    public void createTest() {
        Task expectedTask = task1;
        SubTask expectedSubTask = subTask1;
        Epic expectedEpic = epic1;

        assertEquals(expectedTask, backedManager.getTaskByID(3));
        assertEquals(expectedSubTask, backedManager.getSubTaskByID(2));
        assertEquals(expectedEpic, backedManager.getEpicByID(1));
    }

    @Test
    public void updateTest() {
        String newName = "Новый Эпик1";
        Status newStatus = Status.IN_PROGRESS;
        String newDescription = "Новое описание";

        epic1.setName(newName);
        backedManager.update(epic1);
        subTask1.setStatus(Status.IN_PROGRESS);
        backedManager.update(subTask1);
        task1.setDescription(newDescription);
        backedManager.update(task1);


        assertEquals(newName, backedManager.getEpicByID(1).getName());
        assertEquals(newStatus, backedManager.getSubTaskByID(2).getStatus());
        assertEquals(newDescription, backedManager.getTaskByID(3).getDescription());
    }

    @Test
    public void getAllTypeTasksTest() {
        Task[] expectedArrayOfTasks = {task1, task2, task3};
        SubTask[] expectedArrayOfSubTasks = {subTask1, subTask2};
        Epic[] expectedArrayOfEpics = {epic1};

        assertEquals(expectedArrayOfTasks.length, backedManager.getAllTasks().size());
        assertArrayEquals(expectedArrayOfTasks, backedManager.getAllTasks().toArray());

        assertEquals(expectedArrayOfSubTasks.length, backedManager.getAllSubTasks().size());
        assertArrayEquals(expectedArrayOfSubTasks, backedManager.getAllSubTasks().toArray());

        assertEquals(expectedArrayOfEpics.length, backedManager.getAllEpics().size());
        assertArrayEquals(expectedArrayOfEpics, backedManager.getAllEpics().toArray());
    }

    @Test
    public void deleteAllTypeTasksTest() {
        int expectedTasksListSize = 0;
        int expectedSubTasksListSize = 0;
        int expectedEpicsListSize = 0;

        backedManager.deleteAllTasks();
        backedManager.deleteAllEpics();

        assertEquals(expectedTasksListSize, backedManager.getAllTasks().size());
        assertEquals(expectedTasksListSize, backedManager.getAllSubTasks().size());
        assertEquals(expectedTasksListSize, backedManager.getAllEpics().size());
    }

    @Test
    public void deleteTypeTaskByID() {
        String expectedMessage = "ОТМЕНА УДАЛЕНИЯ -> [задача с указанным ID не найдена]";


        backedManager.deleteTaskByID(111);
    }
}

