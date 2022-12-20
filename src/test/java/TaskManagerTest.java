import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.yandex.practicum.managers.FileBackedTasksManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.io.File;

public abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager backedManager;
    Task task1;
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
    }

    @Test
    public void shouldReturnCreatedTask() {
        Task expectedTask = task1;
        SubTask expectedSubTask = subTask1;
        Epic expectedEpic = epic1;

        assertEquals(expectedTask, backedManager.getTaskByID(3));
        assertEquals(expectedSubTask, backedManager.getSubTaskByID(2));
        assertEquals(expectedEpic, backedManager.getEpicByID(1));
    }

    @Test
    public void shouldReturnUpdatedTask() {
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

}

