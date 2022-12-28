import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.managers.FileBackedTasksManager;

import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.tasks.*;

import java.io.File;

import java.util.ArrayList;
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

    @Test
    public void shouldReturnSameWhenCompareTasksInMemoryWithFromFile() {
        manager.create(epic1);
        manager.create(task1);
        manager.create(subTask1);
        List<Task> expectedTasksList = List.of(epic1, task1, subTask1);
        manager = Managers.getDefaultFileBacked();
        List<Task> actualList = new ArrayList<>();
        actualList.addAll(manager.getAllEpics());
        actualList.addAll(manager.getAllTasks());
        actualList.addAll(manager.getAllSubTasks());
        assertArrayEquals(expectedTasksList.toArray(), actualList.toArray());
    }

    @Test
    public void shouldReturnSameWhenCompareHistoryInMemoryWithFromFile() {
        manager.create(epic1);
        manager.create(task1);
        manager.create(subTask1);
        manager.getTaskByID(task1.getID());
        manager.getEpicByID(epic1.getID());
        List<Task> expectedTasksList = List.of(task1, epic1);
        manager = Managers.getDefaultFileBacked();
        List<Task> actualList = manager.getHistory();
        assertArrayEquals(expectedTasksList.toArray(), actualList.toArray());
    }
}