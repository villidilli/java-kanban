import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.managers.FileBackedTasksManager;

import ru.yandex.practicum.tasks.*;

import java.io.File;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final File file = new File("src/main/resources/Backup.csv");

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        manager = new FileBackedTasksManager(file);
    }

    @Test
    public void shouldRestoreSavedTasksAfterSaveTasks() {
        manager.create(epic1);
        manager.create(task1);
        manager.create(task2);
        manager.create(subTask1);
        manager.create(subTask2);

        assertArrayEquals(new Task[]{task1, task2}, manager.getAllTasks().toArray());
        assertArrayEquals(new SubTask[]{subTask1, subTask2}, manager.getAllSubTasks().toArray());
        assertArrayEquals(new Epic[]{epic1}, manager.getAllEpics().toArray());
    }

    @Test
    public void shouldRestoreHistoryAfterGetTasksById() {
        manager.create(epic1);
        manager.create(task1);
        manager.create(task2);
        manager.create(subTask1);
        manager.create(subTask2);

        manager.getSubTaskByID(subTask1.getID());
        manager.getTaskByID(task1.getID());

        assertArrayEquals(List.of(subTask1, task1).toArray(), manager.getHistory().toArray());
    }

    @Test
    public void shouldReturnEmptyTasksListsWhenNotCreatedTasks() {
        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void shouldReturnEpicWithoutSubtaskFromReload() {
        manager.create(epic1);

        assertEquals(epic1, manager.getAllEpics().get(0));
    }

    @Test
    public void shouldReturnEmptyHistoryWhenNotGetTaskById() {
        manager.create(epic1);
        manager.create(task2);
        manager.create(subTask2);

        assertTrue(manager.getHistory().isEmpty());
    }
}