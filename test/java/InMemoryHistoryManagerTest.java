import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.managers.HistoryManager;
import ru.yandex.practicum.managers.ManagerNotFoundException;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.managers.ManagerNotFoundException.NOT_TRANSFERRED_INPUT;

public class InMemoryHistoryManagerTest {
    private static Map<Integer, Task> tasks;
    private static Map<Integer, SubTask> subTasks;
    private static Map<Integer, Epic> epics;
    HistoryManager manager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private SubTask subTask1;
    private SubTask subTask2;
    private SubTask subTask3;
    private SubTask subTask4;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefaultHistory();
        task1 = new Task(3, "Таск1", "-", ZonedDateTime.of(LocalDateTime.of(
                2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);
        task2 = new Task(4, "Таск2", "-");
        epic1 = new Epic(1, "Эпик1", "-");
        epic2 = new Epic(2, "Эпик2", "-");
        subTask1 = new SubTask(5, "Саб1", "-", 1, ZonedDateTime.of(LocalDateTime.of(
                2022, 2, 2, 0, 0), ZoneId.systemDefault()), 1);
        subTask2 = new SubTask(6, "Саб2", "-", 1, ZonedDateTime.of(LocalDateTime.of(
                2022, 2, 2, 1, 0), ZoneId.systemDefault()), 1);
        subTask3 = new SubTask(7, "Саб3", "-", 1);
        subTask4 = new SubTask(8, "Саб4", "-", 1, ZonedDateTime.of(LocalDateTime.of(
                2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    //void add(Task task)
    @Test
    public void returnThrowExceptionWhenTaskIsNull() {
        task1 = null;
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.add(task1));
        assertEquals(NOT_TRANSFERRED_INPUT, exception.getMessage());
    }

    @Test
    public void returnThrowExceptionWhenSubtaskIsNull() {
        subTask1 = null;
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.add(subTask1));
        assertEquals(NOT_TRANSFERRED_INPUT, exception.getMessage());
    }

    @Test
    public void returnThrowExceptionWhenEpicIsNull() {
        epic1 = null;
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.add(epic1));
        assertEquals(NOT_TRANSFERRED_INPUT, exception.getMessage());
    }

    @Test
    public void returnSameTaskFromHistoryAfterTaskAdded() {
        manager.add(task1);
        assertEquals(task1, manager.getHistory().get(manager.getHistory().size() - 1));
    }

    @Test
    public void returnSameTaskFromHistoryAfterSubtaskAdded() {
        manager.add(subTask1);
        assertEquals(subTask1, manager.getHistory().get(manager.getHistory().size() - 1));
    }

    @Test
    public void returnSameTaskFromHistoryAfterEpicAdded() {
        manager.add(epic1);
        assertEquals(epic1, manager.getHistory().get(manager.getHistory().size() - 1));
    }

    @Test
    public void shouldReturnNumberOfTasksInHistoryWhenAddedDuplicateTask() {
        manager.add(task1);
        manager.add(task1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void shouldReturnNumberOfSubtasksInHistoryWhenAddedDuplicateSubtask() {
        manager.add(subTask1);
        manager.add(subTask1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void shouldReturnNumberOfEpicsInHistoryWhenAddedDuplicateEpic() {
        manager.add(epic1);
        manager.add(epic1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void shouldReturnSameAddedTaskWhenGetLastElementFromHistory() {
        manager.add(epic1);
        manager.add(task1);
        assertEquals(task1, manager.getHistory().get(manager.getHistory().size() - 1));
    }

    @Test
    public void shouldReturnSameAddedSubtaskWhenGetLastElementFromHistory() {
        manager.add(epic1);
        manager.add(task1);
        manager.add(subTask1);
        assertEquals(subTask1, manager.getHistory().get(manager.getHistory().size() - 1));
    }

    @Test
    public void shouldReturnSameAddedEpicWhenGetLastElementFromHistory() {
        manager.add(task1);
        manager.add(subTask1);
        manager.add(epic1);
        assertEquals(epic1, manager.getHistory().get(manager.getHistory().size() - 1));
    }

    //void remove(int id)
    @Test
    public void shouldDoesNotThrowExceptionWhenRemoveAnyTaskByWrongIdIfTaskNotAdded() {
        assertDoesNotThrow(() -> manager.remove(55555));
    }

    @Test
    public void shouldReturnCorrectlyOrderBeforeDeletedAnyTaskFromStartHistory() {
        manager.add(task1);
        manager.add(subTask1);
        manager.add(epic1);
        manager.remove(task1.getID());
        int[] expectedOrder = {subTask1.getID(), epic1.getID()};
        int[] actualOrder = manager.getHistory()
                .stream()
                .mapToInt(Task::getID)
                .toArray();
        assertArrayEquals(expectedOrder, actualOrder);
    }

    @Test
    public void shouldReturnCorrectlyOrderBeforeDeletedAnyTaskFromMiddleHistory() {
        manager.add(task1);
        manager.add(subTask1);
        manager.add(epic1);
        manager.remove(subTask1.getID());
        int[] expectedOrder = {task1.getID(), epic1.getID()};
        int[] actualOrder = manager.getHistory()
                .stream()
                .mapToInt(Task::getID)
                .toArray();
        assertArrayEquals(expectedOrder, actualOrder);
    }

    @Test
    public void shouldReturnCorrectlyOrderBeforeDeletedAnyTaskFromEndHistory() {
        manager.add(task1);
        manager.add(subTask1);
        manager.add(epic1);
        manager.remove(epic1.getID());
        int[] expectedOrder = {task1.getID(), subTask1.getID()};
        int[] actualOrder = manager.getHistory()
                .stream()
                .mapToInt(Task::getID)
                .toArray();
        assertArrayEquals(expectedOrder, actualOrder);
    }

    //void deleteAllTasksByType(Map<Integer, ? extends Task> tasks)
    @Test
    public void shouldThrowExceptionWhenIncomingParameterIsNull() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.deleteAllTasksByType(null));
        assertEquals(NOT_TRANSFERRED_INPUT, exception.getMessage());
    }

    @Test
    public void shouldDoesNotThrowExceptionWhenIncomingParameterIsEmpty() {
        assertDoesNotThrow(() -> manager.deleteAllTasksByType(Map.of()));
    }

    @Test
    public void shouldReturnCorrectlyOrderWhenDeletedAllTasksByTypeFromHistory() {
        tasks.put(task1.getID(), task1);
        tasks.put(task2.getID(), task2);
        subTasks.put(subTask1.getID(), subTask1);
        subTasks.put(subTask2.getID(), subTask2);
        epics.put(epic1.getID(), epic1);
        epics.put(epic2.getID(), epic2);
        manager.add(task1);
        manager.add(epic1);
        manager.add(subTask1);
        manager.add(task2);
        manager.add(epic2);
        manager.add(subTask2);
        manager.deleteAllTasksByType(tasks);
        int[] expectedOrder = {epic1.getID(), subTask1.getID(), epic2.getID(), subTask2.getID()};
        int[] actualOrder = manager.getHistory()
                .stream()
                .mapToInt(Task::getID)
                .toArray();
        assertArrayEquals(expectedOrder, actualOrder);
    }

    @Test
    public void shouldReturnCorrectlyOrderWhenDeletedSubTasksByTypeFromHistory() {
        tasks.put(task1.getID(), task1);
        tasks.put(task2.getID(), task2);
        subTasks.put(subTask1.getID(), subTask1);
        subTasks.put(subTask2.getID(), subTask2);
        epics.put(epic1.getID(), epic1);
        epics.put(epic2.getID(), epic2);

        manager.add(task1);
        manager.add(epic1);
        manager.add(subTask1);
        manager.add(task2);
        manager.add(epic2);
        manager.add(subTask2);

        manager.deleteAllTasksByType(subTasks);
        int[] expectedOrder = {task1.getID(), epic1.getID(), task2.getID(), epic2.getID()};
        int[] actualOrder = manager.getHistory()
                .stream()
                .mapToInt(Task::getID)
                .toArray();
        assertArrayEquals(expectedOrder, actualOrder);
    }

    @Test
    public void shouldReturnCorrectlyOrderWhenDeletedAllEpicByTypeFromHistory() {
        tasks.put(task1.getID(), task1);
        tasks.put(task2.getID(), task2);
        subTasks.put(subTask1.getID(), subTask1);
        subTasks.put(subTask2.getID(), subTask2);
        epics.put(epic1.getID(), epic1);
        epics.put(epic2.getID(), epic2);

        manager.add(task1);
        manager.add(epic1);
        manager.add(subTask1);
        manager.add(task2);
        manager.add(epic2);
        manager.add(subTask2);

        manager.deleteAllTasksByType(epics);
        int[] expectedOrder = {task1.getID(), subTask1.getID(), task2.getID(), subTask2.getID()};
        int[] actualOrder = manager.getHistory()
                .stream()
                .mapToInt(Task::getID)
                .toArray();
        assertArrayEquals(expectedOrder, actualOrder);
    }
}