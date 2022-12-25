import org.junit.jupiter.api.*;

import ru.yandex.practicum.managers.ManagerNotFoundException;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import ru.yandex.practicum.tasks.*;

import java.time.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;


public abstract class TaskManagerTest<T extends TaskManager> {
    TaskManager taskManager;
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
        taskManager = Managers.getDefault();
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
        taskManager.create(task1);
        assertEquals(task1, taskManager.getAllTasks().get(0));
    }

    @Test //  create(Task newTask);
    public void shouldReturnSameTaskFromPrioritizedListAfterCreate() {
        taskManager.create(task1);
        assertEquals(task1, taskManager.getPrioritizedTasks().get(0));
    }

    @Test //create(Task newTask);
    public void shouldReturnIdAfterCreatedTask() {
        taskManager.create(task1);
        assertEquals(1, taskManager.getTaskByID(task1.getID()).getID());
    }

    @Test //  create(Task newTask); null
    public void shouldThrowExceptionWhenCreateTaskNull() {
        task1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.create(task1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test //  create(Task newSubTask); normal
    public void shouldReturnSameSubTaskFromSubtasksListAfterCreate() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getAllSubTasks().get(0));
    }

    @Test //  create(Task newSubtask);
    public void shouldReturnSameSubtaskFromPrioritizedListAfterCreate() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getPrioritizedTasks().get(0));
    }

    @Test //create(Task newSubtask);
    public void shouldReturnIdAfterCreatedSubtask() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(2, taskManager.getAllSubTasks().get(0).getID());
    }

    @Test //  create(Task newSubTask); null
    public void shouldThrowExceptionWhenCreateSubtaskNull() {
        subTask1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.create(subTask1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNotCreateEpicBeforeCreateSubtask() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                                                        () -> taskManager.create(subTask1));
        assertEquals("\nERROR -> [не найден родительский объект с указанным ID]", exception.getMessage());
    }

    @Test //  create(Task newSubTask); availability parentEpic
    public void shouldReturnEpicIdWhenSubTaskCreated() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(epic1.getID(), taskManager.getSubTaskByID(subTask1.getID()).getParentEpicID());
    }

    @Test // create(Epic newEpic); normal
    public void shouldReturnSameEpicFromEpicListAfterCreate() {
        taskManager.create(epic1);
        assertEquals(epic1, taskManager.getAllEpics().get(0));
    }

    @Test //create(Task newSubtask);
    public void shouldReturnIdAfterCreatedEpic() {
        taskManager.create(epic1);
        assertEquals(1, taskManager.getAllEpics().get(0).getID());
    }

    //create(Epic newEpic)
    @Test
    public void shouldThrowExceptionWhenCreateEpicNull() {
        epic1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.create(epic1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldReturnEpicsSubtaskAfterTheyCreated() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getEpicByID(epic1.getID()).getEpicSubTasks().get(subTask1.getID()));
    }

    @Test
    public void shouldReturnEmptyEpicSubtasksListWhenSubtaskNotCreated() {
        taskManager.create(epic1);
        assertTrue(taskManager.getEpicByID(epic1.getID()).getEpicSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEpicStatusNewAfterEpicCreated() {
        taskManager.create(epic1);
        assertEquals(Status.NEW, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    // update(Task newTask)
    @Test
    public void shouldReturnSameTaskFromTasksListAfterUpdate() {
        taskManager.create(task1);
        task1 = new Task(task1.getID(), "Новое имя", "");
        assertEquals(task1, taskManager.getAllTasks().get(0));
    }

    @Test
    public void shouldReturnSameTaskFromPrioritizedListAfterUpdate() {
        taskManager.create(task1);
        task1 = new Task(task1.getID(), "Новое имя", "");
        assertEquals(task1, taskManager.getPrioritizedTasks().get(0));
    }

    @Test
    public void shouldReturnUpdateFieldWhenTaskWasUpdate() {
        taskManager.create(task1);
        String newName = "Updated name";
        task1 = new Task(task1.getID(), newName, task1.getDescription());
        taskManager.update(task1);
        assertEquals(newName, taskManager.getTaskByID(task1.getID()).getName());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateTaskNull() {
        taskManager.create(task1);
        task1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(task1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateIfTaskBeforeNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(task1));
        assertEquals("\nERROR -> [предыдущая версия задачи не найдена]", exception.getMessage());
    }

    // update(SubTask newSubTask)
    @Test
    public void shouldReturnSameSubtaskFromSubtasksListAfterCreated() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getAllSubTasks().get(0));
    }

    @Test
    public void shouldReturnSameSubtaskFromPrioritizedListAfterCreated() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getPrioritizedTasks().get(0));
    }
    @Test
    public void shouldReturnUpdateFieldWhenSubtaskUpdate() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        String newName = "Updated name";
        subTask1 = new SubTask(subTask1.getID(), newName, subTask1.getDescription(), subTask1.getParentEpicID());
        taskManager.update(subTask1);
        assertEquals(newName, taskManager.getSubTaskByID(subTask1.getID()).getName());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateSubtaskNull() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        subTask1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(subTask1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateSubtaskIfBeforeIsNull() {
        taskManager.create(epic1);
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(subTask1));
        assertEquals("\nERROR -> [предыдущая версия задачи не найдена]", actualException.getMessage());
    }

    @Test
    public void shouldReturnEpicIdWhenSubTaskUpdated() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.update(subTask1);
        assertEquals(epic1.getID(), taskManager.getSubTaskByID(subTask1.getID()).getParentEpicID());
    }

    //update(Epic newEpic)
    @Test
    public void shouldReturnSameEpicFromEpicsListAfterCreated() {
        taskManager.create(epic1);
        assertEquals(epic1, taskManager.getAllEpics().get(0));
    }
    @Test
    public void shouldReturnUpdateFieldWhenEpicWasUpdate() {
        taskManager.create(epic1);
        String newName = "Updated name";
        epic1 = new Epic(epic1.getID(), newName, epic1.getDescription());
        taskManager.update(epic1);
        assertEquals(newName, taskManager.getAllEpics().get(0).getName());
    }

    @Test
    public void shouldThrowExceptionBeforeUpdateWhenEpicNull() {
        taskManager.create(epic1);
        epic1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(epic1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUpdateEpicIfBeforeIsNull() {
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(epic1));
        assertEquals("\nERROR -> [предыдущая версия задачи не найдена]", actualException.getMessage());
    }

    @Test
    public void shouldReturnEpicStatusNewWhenSubtasksDone() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
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
        taskManager.update(subTask1);
        taskManager.update(subTask2);
        assertEquals(Status.DONE, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusNewWhenSubtasksNewAndDone() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        subTask1 = new SubTask(
                subTask1.getID(),
                subTask1.getName(),
                subTask1.getDescription(),
                Status.DONE,
                subTask1.getParentEpicID()
        );
        taskManager.update(subTask1);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusNewWhenSubtasksInProgress() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
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
        taskManager.update(subTask1);
        taskManager.update(subTask2);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    //getAllTasks()
    @Test
    public void shouldReturnNumberOfCreatedTasks() {
        taskManager.create(task1);
        taskManager.create(task2);
        assertEquals(2, taskManager.getAllTasks().size());
    }

    @Test
    public void shouldReturnEmptyListWhenTasksNotCreated() {
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    // getAllSubTasks()
    @Test
    public void shouldReturnNumberOfCreatedSubTasks() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        assertEquals(2, taskManager.getAllSubTasks().size());
    }

    @Test
    public void shouldReturnEmptyListWhenSubTasksNotCreated() {
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnNumberOfCreatedEpics() {
        taskManager.create(epic1);
        taskManager.create(epic2);
        assertEquals(2, taskManager.getAllEpics().size());
    }

    @Test
    public void shouldReturnEmptyListWhenEpicsNotCreated() {
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    // deleteAllTasks()
    @Test
    public void shouldReturnEmptyTasksListWhenAllTasksDeleted() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllTasksDeleted() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.deleteAllTasks();
        long actualValue = taskManager.getPrioritizedTasks().stream()
                        .filter(task -> task.getTaskType() == TaskTypes.TASK)
                        .count();
        assertEquals(0, actualValue);
    }

    @Test
    public void shouldReturnEmptyTasksListWhenAllTasksDeletedButTasksNotCreatedBefore() {
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllTasksDeletedButTasksNotCreatedBefore() {
        taskManager.deleteAllTasks();
        long actualValue = taskManager.getPrioritizedTasks().stream()
                .filter(task -> task.getTaskType() == TaskTypes.TASK)
                .count();
        assertEquals(0,actualValue);
    }

    // deleteAllSubTasks()
    @Test
    public void shouldReturnEmptySubtasksListWhenAllSubtasksDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllSubtasksDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllSubTasks();
        long actualValue = taskManager.getPrioritizedTasks().stream()
                .filter(subtask -> subtask.getTaskType() == TaskTypes.SUBTASK)
                .count();
        assertEquals(0, actualValue);
    }

    @Test
    public void shouldReturnEmptySubtasksListWhenAllSubtasksDeletedButSubtasksNotCreatedBefore() {
        taskManager.deleteAllSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllSubtasksDeletedButSubtasksNotCreatedBefore() {
        taskManager.deleteAllSubTasks();
        long actualValue = taskManager.getPrioritizedTasks().stream()
                .filter(subtask -> subtask.getTaskType() == TaskTypes.SUBTASK)
                .count();
        assertEquals(0,actualValue);
    }

    @Test
    public void shouldReturnNumberSubTasksFromEpicSubtasksListWhenAllSubTasksDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getEpicByID(epic1.getID()).getEpicSubTasks().size());
    }

    @Test
    public void shouldReturnEpicStatusNewWhenHisAllSubtasksWithStatusDoneDeleted() {
        taskManager.create(epic1);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllSubTasks();
        assertEquals(Status.NEW, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    // deleteAllEpics()
    @Test
    public void shouldReturnEmptyEpicsListWhenAllEpicsDeleted() {
        taskManager.create(epic1);
        taskManager.create(epic2);
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    public void shouldReturnEmptyEpicsListWhenAllEpicsDeletedButEpicsNotCreatedBefore() {
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptySubtasksListWhenAllEpicsDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyPrioritizedListWhenAllEpicsDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    // getTaskByID(int ID)
    @Test
    public void shouldReturnSameTaskWhenGetTaskById() {
        taskManager.create(task1);
        assertEquals(task1, taskManager.getTaskByID(task1.getID()));
    }

    @Test
    public void shouldThrowExceptionWhenGetTaskByIdIfTaskNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getTaskByID(task1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenGetTaskByIdIfIdWrong() {
        taskManager.create(task1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    // getSubTaskByID(int ID)
    @Test
    public void shouldReturnSameSubTaskWhenGetSubtaskById() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getSubTaskByID(subTask1.getID()));
    }

    @Test
    public void shouldThrowExceptionWhenGetSubtaskByIdIfSubtaskNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenGetSubTaskByIdIfIdWrong() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getSubTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    // getEpicByID(int ID)
    @Test
    public void shouldReturnSameEpicWhenGetEpicById() {
        taskManager.create(epic1);
        assertEquals(epic1, taskManager.getEpicByID(epic1.getID()));
    }

    @Test
    public void shouldThrowExceptionWhenGetEpicByIdIfEpicNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getEpicByID(epic1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenGetEpicByIdIfIdWrong() {
        taskManager.create(epic1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getEpicByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    //deleteTaskByID(int ID)
    @Test
    public void shouldThrowExceptionByTasksListWhenTaskDeleteById() {
        taskManager.create(task1);
        taskManager.deleteTaskByID(task1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getTaskByID(task1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDeleteTaskByWrongId() {
        taskManager.create(task1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.deleteTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldFalseByContainsInPrioritizedListWhenDeleteTaskByID() {
        taskManager.create(task1);
        taskManager.deleteTaskByID(task1.getID());
        assertFalse(taskManager.getPrioritizedTasks().contains(task1));
    }

    // deleteSubTaskByID(int ID)
    @Test
    public void shouldThrowExceptionBySubtasksListWhenSubtaskDeleteById() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.deleteSubTaskByID(subTask1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDeleteSubTaskByWrongId() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.deleteTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldFalseByContainsInPrioritizedListWhenDeleteSubtaskByID() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.deleteSubTaskByID(subTask1.getID());
        assertFalse(taskManager.getPrioritizedTasks().contains(subTask1));
    }

    @Test
    public void shouldReturnEpicStatusNewWhenHisSubTasksWithDoneDeleted() {
        taskManager.create(epic1);
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteSubTaskByID(subTask2.getID());
        assertEquals(Status.NEW, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusDoneWhenHisSubTasksWithNewDeleted() {
        taskManager.create(epic1);
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteSubTaskByID(subTask1.getID());
        assertEquals(Status.DONE, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test
    public void shouldReturnEpicStatusDoneWhenHisSubTasksWithInProgressDeleted() {
        taskManager.create(epic1);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteSubTaskByID(subTask2.getID());
        assertEquals(Status.DONE, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    // deleteEpicByID(int ID)
    @Test
    public void shouldThrowExceptionWhenEpicDeleted() {
        taskManager.create(epic1);
        taskManager.deleteEpicByID(epic1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getEpicByID(epic1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDeleteEpicByWrongId() {
        taskManager.create(epic1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.deleteEpicByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEpicDeletedIfGetHisSubTaskById() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.deleteEpicByID(epic1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                                        () -> taskManager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    //getAllSubTasksByEpic(int ID)
    @Test
    public void shouldReturnNumberOfEpicSubtasksListWhenGetEpicSubtasksList() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        assertEquals(2, taskManager.getAllSubTasksByEpic(epic1.getID()).size());
    }

    @Test
    public void shouldThrowExceptionWhenGetEpicSubtasksListByWrongId() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                                                         () -> taskManager.getAllSubTasksByEpic(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test
    public void shouldReturnEmptyListWhenGetEpicSubtasksListFromEmptyList() {
        taskManager.create(epic1);
        assertTrue(taskManager.getAllSubTasksByEpic(epic1.getID()).isEmpty());
    }

    // getHistory()
    @Test
    public void shouldReturnNumberOfTasksWhenGetHistoryList() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.getTaskByID(task1.getID());
        taskManager.getTaskByID(task2.getID());
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    public void shouldReturnEmptyListWhenGetHistoryFromEmptyList() {
        assertTrue(taskManager.getHistory().isEmpty());
    }

    //getPrioritizedTasks()
    @Test
    public void shouldReturnCorrectlyOrderPriorityWhenGetPrioritizedTasksList() {
        taskManager.create(epic1);
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.create(subTask3);
        taskManager.create(subTask2);

        Task[] expectedArray = {task1, subTask2, task2, subTask3};
        assertArrayEquals(expectedArray, taskManager.getPrioritizedTasks().toArray());
    }

    @Test //getPrioritizedTasks() empty list
    public void shouldReturnEmptyPrioritizedListWhenGetPrioritizedTasksIsEmpty() {
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void shouldAddedTasksAndReturnNumberOfTasksPrioritizedListWhenStartTimeNotSet() {
        task1.setStartTime(Task.UNREACHEBLE_DATE);
        task2.setStartTime(Task.UNREACHEBLE_DATE);
        taskManager.create(task1);
        taskManager.create(task2);
        assertEquals(2, taskManager.getPrioritizedTasks().size());
    }

    @Test
    public void shouldNotAddEpicToPrioritizedListWhenEpicCreated() {
        taskManager.create(epic1);
        assertTrue(taskManager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenTaskHasIntersectionOnStartTime() {
        taskManager.create(epic1);
        taskManager.create(task1);
        TimeValueException exception = assertThrows(TimeValueException.class,
                () -> taskManager.create(subTask4));
        assertEquals("\nERROR -> [Пересечение интервалов выполнения]", exception.getMessage());
    }

    @Test
    public void shouldReplaceTaskInPrioritizedTasksWhenUpdateThisTask() {
        taskManager.create(task1);
        task1 = new Task(task1.getID(), "Новое имя", task2.getDescription());
        taskManager.update(task1);
        assertEquals(1, taskManager.getPrioritizedTasks().size());
        assertEquals("Новое имя", taskManager.getPrioritizedTasks().get(0).getName());
    }

    @Test
    public void shouldNotDeleteTaskInPrioritizedTasksWhenUpdateThisTaskWithSameParameters() {
        taskManager.create(task1);
        taskManager.update(task1);
        assertEquals(1, taskManager.getPrioritizedTasks().size());
        assertEquals(task1, taskManager.getPrioritizedTasks().get(0));
    }

    //updateEpicStartTime(int epicID)
    @Test
    public void shouldReturnEarliestStartTimeOfEpicsSubtasks() {
        taskManager.create(epic1);
        subTask1.setStartTime(startTime3);
        subTask2.setStartTime(startTime1);
        subTask3.setStartTime(startTime2);
        taskManager.create(subTask2);
        taskManager.create(subTask1);
        taskManager.create(subTask3);
        assertEquals(startTime1, epic1.getStartTime());
    }

    //updateEpicDuration(int epicID)
    @Test
    public void shouldReturnSumEpicSubtasksDurations() {
        taskManager.create(epic1);
        subTask1.setDuration(duration2);
        subTask2.setDuration(duration3);
        subTask3.setDuration(duration1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.create(subTask3);
        long expectedSumDurations = duration1 + duration2 + duration3;
        assertEquals(expectedSumDurations, epic1.getDuration());
    }

    //getEndTime
    @Test
    public void shouldReturnEndTimeTask() {
        task1.setStartTime(startTime1);
        task1.setDuration(duration1);
        taskManager.create(task1);
        ZonedDateTime expectedValue = startTime1.plusMinutes(duration1);
        assertEquals(expectedValue, task1.getEndTime());
    }

    @Test
    public void shouldReturnEndTimeSubtask() {
        subTask1.setStartTime(startTime1);
        subTask1.setDuration(duration1);
        taskManager.create(epic1);
        taskManager.create(subTask1);
        ZonedDateTime expectedValue = startTime1.plusMinutes(duration1);
        assertEquals(expectedValue, subTask1.getEndTime());
    }

    @Test
    public void shouldReturnEndTimeEpic() {
        subTask1.setStartTime(startTime1);
        subTask2.setStartTime(startTime2);
        subTask1.setDuration(duration1);
        subTask2.setDuration(duration2);
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
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
