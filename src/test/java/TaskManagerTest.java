import org.junit.jupiter.api.*;

import ru.yandex.practicum.managers.ManagerNotFoundException;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import ru.yandex.practicum.tasks.*;

import java.time.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager manager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    SubTask subTask4;
    ZonedDateTime startTime1;
    ZonedDateTime startTime2;
    ZonedDateTime startTime3;
    long duration1;
    long duration2;
    long duration3;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
        task1 = new Task("Таск1", "-", 2022, 1, 1, 0, 0, 1);
        task2 = new Task("Таск2", "-");
        epic1 = new Epic("Эпик1", "-");
        epic2 = new Epic("Эпик2", "-");
        subTask1 = new SubTask("СабТаск1", "-", 1,
                2022, 2, 2, 0, 0, 1);
        subTask2 = new SubTask("СабТаск2", "-", 1,
                2022, 2, 2, 1, 0, 1);
        subTask3 = new SubTask("СабТаск3", "-", 1);
        subTask4 = new SubTask("СабТаск4", "-", 1,
                2022, 1, 1, 0, 0, 1);
        startTime1 = ZonedDateTime.of(
                LocalDate.of(2021, 12, 12),
                LocalTime.of(0,0),
                ZoneId.systemDefault()
        );
        startTime2 = ZonedDateTime.of(
                LocalDate.of(2022, 6, 6),
                LocalTime.of(12,0),
                ZoneId.systemDefault()
        );
        startTime3 = ZonedDateTime.of(
                LocalDate.of(2023, 1, 1),
                LocalTime.of(23,59),
                ZoneId.systemDefault()
        );
        duration1 = 30L;
        duration2 = 10L;
        duration3 = 20L;
    }

    @Test //  create(Task newTask);
    public void shouldReturnSameTaskFromTasksListAfterCreate() {
        manager.create(task1);
        assertEquals(task1, manager.getAllTasks().get(0));
    }

    @Test //  create(Task newTask);
    public void shouldReturnSameTaskFromPrioritizedListAfterCreate() {
        manager.create(task1);
        assertEquals(task1, manager.getPrioritizedTasks().get(0));
    }

    @Test //create(Task newTask);
    public void shouldReturnIdAfterCreatedTask() {
        manager.create(task1);
        assertEquals(1, manager.getTaskByID(task1.getID()).getID());
    }

    @Test //  create(Task newTask); null
    public void shouldThrowExceptionWhenCreateTaskNull() {
        task1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> manager.create(task1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test //  create(Task newSubTask); normal
    public void shouldReturnSameSubTaskFromSubtasksListAfterCreate() {
        manager.create(epic1);
        manager.create(subTask1);
        assertEquals(subTask1, manager.getAllSubTasks().get(0));
    }

    @Test //  create(Task newSubtask);
    public void shouldReturnSameSubtaskFromPrioritizedListAfterCreate() {
        manager.create(epic1);
        manager.create(subTask1);
        assertEquals(subTask1, manager.getPrioritizedTasks().get(0));
    }

    @Test //create(Task newSubtask);
    public void shouldReturnIdAfterCreatedSubtask() {
        manager.create(epic1);
        manager.create(subTask1);
        assertEquals(2, manager.getAllSubTasks().get(0).getID());
    }

    @Test //  create(Task newSubTask); null
    public void shouldThrowExceptionWhenCreateSubtaskNull() {
        subTask1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> manager.create(subTask1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNotCreateEpicBeforeCreateSubtask() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                                                        () -> manager.create(subTask1));
        assertEquals("\nERROR -> [не найден родительский объект с указанным ID]", exception.getMessage());
    }

    @Test //  create(Task newSubTask); availability parentEpic
    public void shouldReturnEpicIdWhenSubTaskCreated() {
        manager.create(epic1);
        manager.create(subTask1);
        assertEquals(epic1.getID(), manager.getSubTaskByID(subTask1.getID()).getParentEpicID());
    }

    @Test // create(Epic newEpic); normal
    public void shouldReturnSameEpicFromEpicListAfterCreate() {
        manager.create(epic1);
        assertEquals(epic1, manager.getAllEpics().get(0));
    }

    @Test //create(Task newSubtask);
    public void shouldReturnIdAfterCreatedEpic() {
        manager.create(epic1);
        assertEquals(1, manager.getAllEpics().get(0).getID());
    }

    //create(Epic newEpic)
    @Test
    public void shouldThrowExceptionWhenCreateEpicNull() {
        epic1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> manager.create(epic1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldReturnEpicsSubtaskAfterTheyCreated() {
        manager.create(epic1);
        manager.create(subTask1);
        assertEquals(subTask1, manager.getEpicByID(epic1.getID()).getEpicSubTasks().get(subTask1.getID()));
    }

    @Test
    public void shouldReturnEmptyEpicSubtasksListWhenSubtaskNotCreated() {
        manager.create(epic1);
        assertTrue(manager.getEpicByID(epic1.getID()).getEpicSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEpicStatusNewAfterEpicCreated() {
        manager.create(epic1);
        assertEquals(Status.NEW, manager.getEpicByID(epic1.getID()).getStatus());
    }

    // update(Task newTask)
    @Test
    public void shouldReturnSameTaskFromTasksListAfterUpdate() {
        manager.create(task1);
        task1 = new Task(task1.getID(), "Новое имя", "");
        assertEquals(task1, manager.getAllTasks().get(0));
    }

    @Test
    public void shouldReturnSameTaskFromPrioritizedListAfterUpdate() {
        manager.create(task1);
        task1 = new Task(task1.getID(), "Новое имя", "");
        assertEquals(task1, manager.getPrioritizedTasks().get(0));
    }

    @Test
    public void shouldReturnUpdateFieldWhenTaskWasUpdate() {
        manager.create(task1);
        String newName = "Updated name";
        task1 = new Task(task1.getID(), newName, task1.getDescription());
        manager.update(task1);
        assertEquals(newName, manager.getTaskByID(task1.getID()).getName());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateTaskNull() {
        manager.create(task1);
        task1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> manager.update(task1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateIfTaskBeforeNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.update(task1));
        assertEquals("\nERROR -> [предыдущая версия задачи не найдена]", exception.getMessage());
    }

    // update(SubTask newSubTask)
    @Test
    public void shouldReturnSameSubtaskFromSubtasksListAfterCreated() {
        manager.create(epic1);
        manager.create(subTask1);
        assertEquals(subTask1, manager.getAllSubTasks().get(0));
    }

    @Test
    public void shouldReturnSameSubtaskFromPrioritizedListAfterCreated() {
        manager.create(epic1);
        manager.create(subTask1);
        assertEquals(subTask1, manager.getPrioritizedTasks().get(0));
    }
    @Test
    public void shouldReturnUpdateFieldWhenSubtaskUpdate() {
        manager.create(epic1);
        manager.create(subTask1);
        String newName = "Updated name";
        subTask1 = new SubTask(subTask1.getID(), newName, subTask1.getDescription(), subTask1.getParentEpicID());
        manager.update(subTask1);
        assertEquals(newName, manager.getSubTaskByID(subTask1.getID()).getName());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateSubtaskNull() {
        manager.create(epic1);
        manager.create(subTask1);
        subTask1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> manager.update(subTask1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateSubtaskIfBeforeIsNull() {
        manager.create(epic1);
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> manager.update(subTask1));
        assertEquals("\nERROR -> [предыдущая версия задачи не найдена]", actualException.getMessage());
    }

    @Test
    public void shouldReturnEpicIdWhenSubTaskUpdated() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.update(subTask1);
        assertEquals(epic1.getID(), manager.getSubTaskByID(subTask1.getID()).getParentEpicID());
    }

    //update(Epic newEpic)
    @Test
    public void shouldReturnSameEpicFromEpicsListAfterCreated() {
        manager.create(epic1);
        assertEquals(epic1, manager.getAllEpics().get(0));
    }
    @Test
    public void shouldReturnUpdateFieldWhenEpicWasUpdate() {
        manager.create(epic1);
        String newName = "Updated name";
        epic1 = new Epic(epic1.getID(), newName, epic1.getDescription());
        manager.update(epic1);
        assertEquals(newName, manager.getAllEpics().get(0).getName());
    }

    @Test
    public void shouldThrowExceptionBeforeUpdateWhenEpicNull() {
        manager.create(epic1);
        epic1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> manager.update(epic1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateEpicIfBeforeIsNull() {
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> manager.update(epic1));
        assertEquals("\nERROR -> [предыдущая версия задачи не найдена]", actualException.getMessage());
    }

    @Test
    public void shouldReturnEpicStatusNewWhenSubtasksDone() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        subTask1 = new SubTask(
                subTask1.getID(),
                subTask1.getName(),
                subTask1.getDescription(),
                Status.DONE,
                subTask1.getParentEpicID()
        );
        subTask2 = new SubTask(
                subTask2.getID(),
                subTask2.getName(),
                subTask2.getDescription(),
                Status.DONE,
                subTask2.getParentEpicID()
        );
        manager.update(subTask1);
        manager.update(subTask2);
        assertEquals(Status.DONE, manager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusNewWhenSubtasksNewAndDone() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        subTask1 = new SubTask(
                subTask1.getID(),
                subTask1.getName(),
                subTask1.getDescription(),
                Status.DONE,
                subTask1.getParentEpicID()
        );
        manager.update(subTask1);
        assertEquals(Status.IN_PROGRESS, manager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusNewWhenSubtasksInProgress() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        subTask1 = new SubTask(
                subTask1.getID(),
                subTask1.getName(),
                subTask1.getDescription(),
                Status.IN_PROGRESS,
                subTask1.getParentEpicID()
        );
        subTask2 = new SubTask(
                subTask2.getID(),
                subTask2.getName(),
                subTask2.getDescription(),
                Status.IN_PROGRESS,
                subTask2.getParentEpicID()
        );
        manager.update(subTask1);
        manager.update(subTask2);
        assertEquals(Status.IN_PROGRESS, manager.getEpicByID(epic1.getID()).getStatus());
    }

    //getAllTasks()
    @Test
    public void shouldReturnNumberOfCreatedTasks() {
        manager.create(task1);
        manager.create(task2);
        assertEquals(2, manager.getAllTasks().size());
    }

    @Test
    public void shouldReturnEmptyListWhenTasksNotCreated() {
        assertTrue(manager.getAllTasks().isEmpty());
    }

    // getAllSubTasks()
    @Test
    public void shouldReturnNumberOfCreatedSubTasks() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        assertEquals(2, manager.getAllSubTasks().size());
    }

    @Test
    public void shouldReturnEmptyListWhenSubTasksNotCreated() {
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnNumberOfCreatedEpics() {
        manager.create(epic1);
        manager.create(epic2);
        assertEquals(2, manager.getAllEpics().size());
    }

    @Test
    public void shouldReturnEmptyListWhenEpicsNotCreated() {
        assertTrue(manager.getAllEpics().isEmpty());
    }

    // deleteAllTasks()
    @Test
    public void shouldReturnEmptyTasksListWhenAllTasksDeleted() {
        manager.create(task1);
        manager.create(task2);
        manager.deleteAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllTasksDeleted() {
        manager.create(task1);
        manager.create(task2);
        manager.deleteAllTasks();
        long actualValue = manager.getPrioritizedTasks().stream()
                        .filter(task -> task.getTaskType() == TaskTypes.TASK)
                        .count();
        assertEquals(0, actualValue);
    }

    @Test
    public void shouldReturnEmptyTasksListWhenAllTasksDeletedButTasksNotCreatedBefore() {
        manager.deleteAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllTasksDeletedButTasksNotCreatedBefore() {
        manager.deleteAllTasks();
        long actualValue = manager.getPrioritizedTasks().stream()
                .filter(task -> task.getTaskType() == TaskTypes.TASK)
                .count();
        assertEquals(0,actualValue);
    }

    // deleteAllSubTasks()
    @Test
    public void shouldReturnEmptySubtasksListWhenAllSubtasksDeleted() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteAllSubTasks();
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllSubtasksDeleted() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteAllSubTasks();
        long actualValue = manager.getPrioritizedTasks().stream()
                .filter(subtask -> subtask.getTaskType() == TaskTypes.SUBTASK)
                .count();
        assertEquals(0, actualValue);
    }

    @Test
    public void shouldReturnEmptySubtasksListWhenAllSubtasksDeletedButSubtasksNotCreatedBefore() {
        manager.deleteAllSubTasks();
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllSubtasksDeletedButSubtasksNotCreatedBefore() {
        manager.deleteAllSubTasks();
        long actualValue = manager.getPrioritizedTasks().stream()
                .filter(subtask -> subtask.getTaskType() == TaskTypes.SUBTASK)
                .count();
        assertEquals(0,actualValue);
    }

    @Test
    public void shouldReturnNumberSubTasksFromEpicSubtasksListWhenAllSubTasksDeleted() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteAllSubTasks();
        assertEquals(0, manager.getEpicByID(epic1.getID()).getEpicSubTasks().size());
    }

    @Test
    public void shouldReturnEpicStatusNewWhenHisAllSubtasksWithStatusDoneDeleted() {
        manager.create(epic1);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteAllSubTasks();
        assertEquals(Status.NEW, manager.getEpicByID(epic1.getID()).getStatus());
    }

    // deleteAllEpics()
    @Test
    public void shouldReturnEmptyEpicsListWhenAllEpicsDeleted() {
        manager.create(epic1);
        manager.create(epic2);
        manager.deleteAllEpics();
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    public void shouldReturnEmptyEpicsListWhenAllEpicsDeletedButEpicsNotCreatedBefore() {
        manager.deleteAllEpics();
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptySubtasksListWhenAllEpicsDeleted() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteAllEpics();
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllEpicsDeleted() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteAllEpics();
        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    // getTaskByID(int ID)
    @Test
    public void shouldReturnSameTaskWhenGetTaskById() {
        manager.create(task1);
        assertEquals(task1, manager.getTaskByID(task1.getID()));
    }

    @Test
    public void shouldThrowExceptionWhenGetTaskByIdIfTaskNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getTaskByID(task1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenGetTaskByIdIfIdWrong() {
        manager.create(task1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    // getSubTaskByID(int ID)
    @Test
    public void shouldReturnSameSubTaskWhenGetSubtaskById() {
        manager.create(epic1);
        manager.create(subTask1);
        assertEquals(subTask1, manager.getSubTaskByID(subTask1.getID()));
    }

    @Test
    public void shouldThrowExceptionWhenGetSubtaskByIdIfSubtaskNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenGetSubTaskByIdIfIdWrong() {
        manager.create(epic1);
        manager.create(subTask1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getSubTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    // getEpicByID(int ID)
    @Test
    public void shouldReturnSameEpicWhenGetEpicById() {
        manager.create(epic1);
        assertEquals(epic1, manager.getEpicByID(epic1.getID()));
    }

    @Test
    public void shouldThrowExceptionWhenGetEpicByIdIfEpicNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getEpicByID(epic1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenGetEpicByIdIfIdWrong() {
        manager.create(epic1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getEpicByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    //deleteTaskByID(int ID)
    @Test
    public void shouldThrowExceptionByTasksListWhenTaskDeleteById() {
        manager.create(task1);
        manager.deleteTaskByID(task1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getTaskByID(task1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDeleteTaskByWrongId() {
        manager.create(task1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.deleteTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldFalseByContainsInPrioritizedListWhenDeleteTaskByID() {
        manager.create(task1);
        manager.deleteTaskByID(task1.getID());
        assertFalse(manager.getPrioritizedTasks().contains(task1));
    }

    // deleteSubTaskByID(int ID)
    @Test
    public void shouldThrowExceptionBySubtasksListWhenSubtaskDeleteById() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.deleteSubTaskByID(subTask1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDeleteSubTaskByWrongId() {
        manager.create(epic1);
        manager.create(subTask1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.deleteTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldFalseByContainsInPrioritizedListWhenDeleteSubtaskByID() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.deleteSubTaskByID(subTask1.getID());
        assertFalse(manager.getPrioritizedTasks().contains(subTask1));
    }

    @Test
    public void shouldReturnEpicStatusNewWhenHisSubTasksWithDoneDeleted() {
        manager.create(epic1);
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteSubTaskByID(subTask2.getID());
        assertEquals(Status.NEW, manager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusDoneWhenHisSubTasksWithNewDeleted() {
        manager.create(epic1);
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteSubTaskByID(subTask1.getID());
        assertEquals(Status.DONE, manager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusDoneWhenHisSubTasksWithInProgressDeleted() {
        manager.create(epic1);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.IN_PROGRESS);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.deleteSubTaskByID(subTask2.getID());
        assertEquals(Status.DONE, manager.getEpicByID(epic1.getID()).getStatus());
    }

    // deleteEpicByID(int ID)
    @Test
    public void shouldThrowExceptionWhenEpicDeleted() {
        manager.create(epic1);
        manager.deleteEpicByID(epic1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.getEpicByID(epic1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDeleteEpicByWrongId() {
        manager.create(epic1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> manager.deleteEpicByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEpicDeletedIfGetHisSubTaskById() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.deleteEpicByID(epic1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                                        () -> manager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    //getAllSubTasksByEpic(int ID)
    @Test
    public void shouldReturnNumberOfEpicSubtasksListWhenGetEpicSubtasksList() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        assertEquals(2, manager.getAllSubTasksByEpic(epic1.getID()).size());
    }

    @Test
    public void shouldThrowExceptionWhenGetEpicSubtasksListByWrongId() {
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                                                         () -> manager.getAllSubTasksByEpic(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldReturnEmptyListWhenGetEpicSubtasksListFromEmptyList() {
        manager.create(epic1);
        assertTrue(manager.getAllSubTasksByEpic(epic1.getID()).isEmpty());
    }

    // getHistory()
    @Test
    public void shouldReturnNumberOfTasksWhenGetHistoryList() {
        manager.create(task1);
        manager.create(task2);
        manager.getTaskByID(task1.getID());
        manager.getTaskByID(task2.getID());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void shouldReturnEmptyListWhenGetHistoryFromEmptyList() {
        assertTrue(manager.getHistory().isEmpty());
    }

    //getPrioritizedTasks()
    @Test
    public void shouldReturnCorrectlyOrderPriorityWhenGetPrioritizedTasksList() {
        manager.create(epic1);
        manager.create(task1);
        manager.create(task2);
        manager.create(subTask3);
        manager.create(subTask2);

        Task[] expectedArray = {task1, subTask2, task2, subTask3};
        assertArrayEquals(expectedArray, manager.getPrioritizedTasks().toArray());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenGetPrioritizedTasksIsEmpty() {
        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void shouldAddedTasksAndReturnNumberOfTasksPrioritizedListWhenStartTimeNotSet() {
        task1.setStartTime(Task.UNREACHEBLE_DATE);
        task2.setStartTime(Task.UNREACHEBLE_DATE);
        manager.create(task1);
        manager.create(task2);
        assertEquals(2, manager.getPrioritizedTasks().size());
    }

    @Test
    public void shouldNotAddEpicToPrioritizedListWhenEpicCreated() {
        manager.create(epic1);
        assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenTaskHasIntersectionOnStartTime() {
        manager.create(epic1);
        manager.create(task1);
        TimeValueException exception = assertThrows(TimeValueException.class,
                () -> manager.create(subTask4));
        assertEquals("\nERROR -> [Пересечение интервалов выполнения]", exception.getMessage());
    }

    @Test
    public void shouldReplaceTaskInPrioritizedTasksWhenUpdateThisTask() {
        manager.create(task1);
        task1 = new Task(task1.getID(), "Новое имя", task2.getDescription());
        manager.update(task1);
        assertEquals(1, manager.getPrioritizedTasks().size());
        assertEquals("Новое имя", manager.getPrioritizedTasks().get(0).getName());
    }

    @Test
    public void shouldNotDeleteTaskInPrioritizedTasksWhenUpdateThisTaskWithSameParameters() {
        manager.create(task1);
        manager.update(task1);
        assertEquals(1, manager.getPrioritizedTasks().size());
        assertEquals(task1, manager.getPrioritizedTasks().get(0));
    }

    //updateEpicStartTime(int epicID)
    @Test
    public void shouldReturnEarliestStartTimeOfEpicsSubtasks() {
        manager.create(epic1);
        subTask1.setStartTime(startTime3);
        subTask2.setStartTime(startTime1);
        subTask3.setStartTime(startTime2);
        manager.create(subTask2);
        manager.create(subTask1);
        manager.create(subTask3);
        assertEquals(startTime1, epic1.getStartTime());
    }

    //updateEpicDuration(int epicID)
    @Test
    public void shouldReturnSumEpicSubtasksDurations() {
        manager.create(epic1);
        subTask1.setDuration(duration2);
        subTask2.setDuration(duration3);
        subTask3.setDuration(duration1);
        manager.create(subTask1);
        manager.create(subTask2);
        manager.create(subTask3);
        long expectedSumDurations = duration1 + duration2 + duration3;
        assertEquals(expectedSumDurations, epic1.getDuration());
    }

    //getEndTime
    @Test
    public void shouldReturnEndTimeTask() {
        task1.setStartTime(startTime1);
        task1.setDuration(duration1);
        manager.create(task1);
        ZonedDateTime expectedValue = startTime1.plusMinutes(duration1);
        assertEquals(expectedValue, task1.getEndTime());
    }

    @Test
    public void shouldReturnEndTimeSubtask() {
        subTask1.setStartTime(startTime1);
        subTask1.setDuration(duration1);
        manager.create(epic1);
        manager.create(subTask1);
        ZonedDateTime expectedValue = startTime1.plusMinutes(duration1);
        assertEquals(expectedValue, subTask1.getEndTime());
    }

    @Test
    public void shouldReturnEndTimeEpic() {
        subTask1.setStartTime(startTime1);
        subTask2.setStartTime(startTime2);
        subTask1.setDuration(duration1);
        subTask2.setDuration(duration2);
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
        ZonedDateTime minStartTime = epic1.getEpicSubTasks().values()
                .stream()
                .min(Comparator.comparing(Task::getStartTime))
                .get()
                .getStartTime();
        long sumDurations = epic1.getEpicSubTasks().values()
                .stream()
                .mapToLong(SubTask::getDuration)
                .sum();
        ZonedDateTime expectedValue = minStartTime.plusMinutes(sumDurations);
        assertEquals(expectedValue, epic1.getEndTime());
    }
}
