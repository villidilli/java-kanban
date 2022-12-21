import org.junit.jupiter.api.*;

import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EpicTest {
    TaskManager manager;
    Epic epic1;
    SubTask subTask1;
    SubTask subTask2;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
        epic1 = new Epic("Эпик1", "-");
        manager.create(epic1);
        subTask1 = new SubTask("Саб1", "-",epic1.getID());
        manager.create(subTask1);
        subTask2 = new SubTask("Саб2", "-",epic1.getID());
        manager.create(subTask2);
    }

    @Test
    public void shouldReturnStatusNewWhenEpicsListOfSubtaskIsEmpty() {
        Status expectedStatus = Status.NEW;
        assertEquals(expectedStatus, epic1.getStatus());
    }

    @Test
    public void shouldReturnStatusNewWhenEpicsListOfSubtaskHaveStatusNew() {
        Status expectedStatus = Status.NEW;
        assertEquals(expectedStatus, epic1.getStatus());
    }

    @Test
    public void shouldReturnStatusDONEWhenEpicsListOfSubtaskHaveStatusDONE() {
        subTask1.setStatus(Status.DONE);
        manager.update(subTask1);
        subTask2.setStatus(Status.DONE);
        manager.update(subTask2);

        Status expectedStatus = Status.DONE;
        assertEquals(expectedStatus, epic1.getStatus());
    }

    @Test
    public void shouldReturnStatusDONEWhenEpicsListOfSubtaskHaveStatusNEWandDONE() {
        subTask2.setStatus(Status.DONE);
        manager.update(subTask2);

        Status expectedStatus = Status.IN_PROGRESS;
        assertEquals(expectedStatus, epic1.getStatus());
    }

    @Test
    public void shouldReturnStatusDONEWhenEpicsListOfSubtaskHaveStatusInProgress() {
        subTask1.setStatus(Status.IN_PROGRESS);
        manager.update(subTask1);
        subTask2.setStatus(Status.IN_PROGRESS);
        manager.update(subTask2);

        Status expectedStatus = Status.IN_PROGRESS;
        assertEquals(expectedStatus, epic1.getStatus());
    }
}
