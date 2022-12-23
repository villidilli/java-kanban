import org.junit.jupiter.api.*;

import ru.yandex.practicum.managers.FileBackedTasksManager;
import ru.yandex.practicum.managers.TaskManager;

import ru.yandex.practicum.tasks.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    TaskManager backedManager;
    Epic epic1;
    SubTask subTask1;
    SubTask subTask2;

    @BeforeEach
    public void beforeEach() {
        backedManager = new FileBackedTasksManager(new File("src/main/resources/Backup.csv"));
    }

    @Test
    public void shouldReturnEpicNewWhenEpicNotHaveSubtasks() {
        Status expectedValue = Status.NEW;

        epic1 = new Epic("Эпик1", "-");
        backedManager.create(epic1);

        assertEquals(expectedValue, backedManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicNewWhenHimSubTasksNew() {
        Status expectedValue = Status.NEW;

        epic1 = new Epic("Эпик1", "-");
        backedManager.create(epic1);
        subTask1 = new SubTask("Саб1", "-",epic1.getID(),
                2022, 1, 1, 0, 0 ,1);
        backedManager.create(subTask1);
        subTask2 = new SubTask("Саб2", "-",epic1.getID(),
                2022, 1, 1, 1, 0 ,1);
        backedManager.create(subTask2);

        assertEquals(expectedValue, backedManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicDoneWhenHimSubTasksDone() {
        Status expectedValue = Status.DONE;

        epic1 = new Epic("Эпик1", "-");
        backedManager.create(epic1);
        subTask1 = new SubTask("Саб1", "-", epic1.getID(),
                2022, 1, 1, 0, 0 ,1);
        subTask1.setStatus(Status.DONE);
        backedManager.create(subTask1);
        subTask2 = new SubTask("Саб2", "-",epic1.getID(),
                2022, 1, 1, 1, 0 ,1);
        subTask2.setStatus(Status.DONE);
        backedManager.create(subTask2);

        assertEquals(expectedValue, backedManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicInProgressWhenHimSubtasksIsNewAndDone() {
        Status expectedValue = Status.IN_PROGRESS;

        epic1 = new Epic("Эпик1", "-");
        backedManager.create(epic1);
        subTask1 = new SubTask("Саб1", "-", epic1.getID(),
                2022, 1, 1, 0, 0 ,1);
        backedManager.create(subTask1);
        subTask2 = new SubTask("Саб2", "-",epic1.getID(),
                2022, 1, 1, 1, 0 ,1);
        subTask2.setStatus(Status.DONE);
        backedManager.create(subTask2);

        assertEquals(expectedValue, backedManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicInProgressWhenHimSubTasksInProgress() {
        Status expectedValue = Status.IN_PROGRESS;

        epic1 = new Epic("Эпик1", "-");
        backedManager.create(epic1);
        subTask1 = new SubTask("Саб1", "-", epic1.getID(),
                2022, 1, 1, 0, 0 ,1);
        subTask1.setStatus(Status.IN_PROGRESS);
        backedManager.create(subTask1);
        subTask2 = new SubTask("Саб2", "-",epic1.getID(),
                2022, 1, 1, 1, 0 ,1);
        subTask2.setStatus(Status.IN_PROGRESS);
        backedManager.create(subTask2);

        assertEquals(expectedValue, backedManager.getEpicByID(epic1.getID()).getStatus());
    }
}
