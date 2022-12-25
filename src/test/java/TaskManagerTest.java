import org.junit.jupiter.api.*;

import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.managers.ManagerNotFoundException;
import ru.yandex.practicum.managers.ManagerSaveException;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import ru.yandex.practicum.tasks.*;

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
        subTask3 = new SubTask("СабТаск2", "-", 1,
                2022, 2, 2, 2, 0, 1);
    }

    @Test // boolean create(Task newTask); standard behavior
    public void shouldReturnSameTaskAfterCreate() {
        taskManager.create(task1);
        assertEquals(task1, taskManager.getTaskByID(task1.getID()));
    }

    @Test // boolean create(Task newTask); wrong id
    public void shouldThrowExceptionWhenTaskNull() {
        task1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.create(task1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test // boolean create(Task newSubTask); standard behavior
    public void shouldReturnSameSubTaskAfterCreate() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getSubTaskByID(subTask1.getID()));
    }

    @Test // boolean create(Task newSubTask); wrong id
    public void shouldThrowExceptionWhenSubTaskNull() {
        subTask1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.create(subTask1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test // boolean create(Task newSubTask); availability epic
    public void shouldReturnEpicIdNewWhenSubTaskCreated() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(epic1.getID(), taskManager.getSubTaskByID(subTask1.getID()).getParentEpicID());
    }

    @Test //boolean create(Epic newEpic); standard behavior
    public void shouldReturnSameEpicAfterCreate() {
        taskManager.create(epic1);
        assertEquals(epic1, taskManager.getEpicByID(epic1.getID()));
    }

    @Test // boolean create(Epic newEpic); wrong id
    public void shouldThrowExceptionWhenEpicNull() {
        epic1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.create(epic1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test // boolean create(Epic newEpic); status test
    public void shouldReturnEpicStatusNewWhenSubTasksNew() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        assertEquals(Status.NEW, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test // boolean update(Task newTask); standard behavior
    public void shouldReturnUpdateFieldWhenTaskWasUpdate() {
        taskManager.create(task1);
        String newName = "Updated name";
        task1 = new Task(task1.getID(), newName, task1.getDescription());
        taskManager.update(task1);
        assertEquals(newName, taskManager.getTaskByID(task1.getID()).getName());
    }

    @Test // boolean update(Task newTask); wrong id
    public void shouldThrowExceptionBeforeUpdateWhenTaskNull() {
        taskManager.create(task1);
        task1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(task1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test // boolean update(SubTask newSubTask); standard behavior
    public void shouldReturnUpdateFieldWhenSubTaskWasUpdate() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        String newName = "Updated name";
        subTask1 = new SubTask(subTask1.getID(), newName, subTask1.getDescription(), subTask1.getParentEpicID());
        taskManager.update(subTask1);
        assertEquals(newName, taskManager.getSubTaskByID(subTask1.getID()).getName());
    }

    @Test // boolean update(SubTask newSubTask); wrong id
    public void shouldThrowExceptionBeforeUpdateWhenSubTaskNull() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        subTask1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(subTask1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test // boolean update(SubTask newSubTask); availability epic
    public void shouldReturnEpicIdWhenSubTaskUpdated() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.update(subTask1);
        assertEquals(epic1.getID(), taskManager.getSubTaskByID(subTask1.getID()).getParentEpicID());
    }

    @Test // boolean update(Epic newEpic); standard behavior
    public void shouldReturnUpdateFieldWhenEpicWasUpdate() {
        taskManager.create(epic1);
        String newName = "Updated name";
        epic1 = new Epic(epic1.getID(), newName, epic1.getDescription());
        taskManager.update(epic1);
        assertEquals(newName, taskManager.getEpicByID(epic1.getID()).getName());
    }

    @Test // boolean update(Epic newEpic); wrong id
    public void shouldThrowExceptionBeforeUpdateWhenEpicNull() {
        taskManager.create(epic1);
        epic1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.update(epic1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test // boolean update(Epic newEpic); check epic status
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

    @Test // boolean update(Epic newEpic); check epic status
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

    @Test // boolean update(Epic newEpic); check epic status
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

    @Test //List<Task> getAllTasks(); standard behavior
    public void shouldReturnNumberOfCreatedTasks() {
        taskManager.create(task1);
        taskManager.create(task2);
        assertEquals(2, taskManager.getAllTasks().size());
    }

    @Test //List<Task> getAllTasks(); empty list
    public void shouldReturnEmptyListWhenTasksNotCreated() {
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test //List<SubTask> getAllSubTasks(); standard behavior
    public void shouldReturnNumberOfCreatedSubTasks() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        assertEquals(2, taskManager.getAllSubTasks().size());
    }

    @Test //List<SubTask> getAllSubTasks(); empty list
    public void shouldReturnEmptyListWhenSubTasksNotCreated() {
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test //List<Epic> getAllEpics(); standard behavior
    public void shouldReturnNumberOfCreatedEpics() {
        taskManager.create(epic1);
        taskManager.create(epic2);
        assertEquals(2, taskManager.getAllEpics().size());
    }

    @Test //List<Epic> getAllEpics(); empty list
    public void shouldReturnEmptyListWhenEpicsNotCreated() {
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test //boolean deleteAllTasks(); standard behavior
    public void shouldReturnEmptyListWhenAllTasksDeleted() {
        taskManager.create(task1);
        taskManager.create(task2);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test //boolean deleteAllTasks(); empty list
    public void shouldReturnEmptyListWhenAllTasksDeletedButTasksNotCreatedBefore() {
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test //boolean deleteAllSubTasks(); standard behavior
    public void shouldReturnEmptyListWhenAllSubTasksDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test //boolean deleteAllSubTasks(); empty list
    public void shouldReturnEmptyListWhenAllSubTasksDeletedButSubTasksNotCreatedBefore() {
        taskManager.deleteAllSubTasks();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test //boolean deleteAllSubTasks(); additional test
    public void shouldReturnNumberOfSubTasksFromEpicSubtaskListWhenAllSubTasksDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        assertEquals(2, taskManager.getEpicByID(epic1.getID()).getEpicSubTasks().size());
    }

    @Test //boolean deleteAllSubTasks(); additional test
    public void shouldReturnEpicStatusNewWhenHisAllSubtasksWithStatusDoneDeleted() {
        taskManager.create(epic1);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllSubTasks();
        assertEquals(Status.NEW, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test //boolean deleteEpics(); standard behavior
    public void shouldReturnEmptyListWhenAllEpicsDeleted() {
        taskManager.create(epic1);
        taskManager.create(epic2);
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test //boolean deleteAllEpics(); empty list
    public void shouldReturnEmptyListWhenAllEpicsDeletedButEpicsNotCreatedBefore() {
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test //boolean deleteAllEpics(); additional test
    public void shouldReturnEmptySubTasksListWhenAllEpicsDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test //Task getTaskByID(int ID); normal behavior
    public void shouldReturnSameTaskWhenGetHimById() {
        taskManager.create(task1);
        assertEquals(task1, taskManager.getTaskByID(task1.getID()));
    }

    @Test //Task getTaskByID(int ID); empty list
    public void shouldThrowExceptionWhenGetTaskByIdAndTaskNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getTaskByID(task1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //Task getTaskByID(int ID); wrong id
    public void shouldThrowExceptionWhenGetTaskByIdAndTaskIsNull() {
        taskManager.create(task1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getTaskByID(5));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //SubTask getSubTaskByID(int ID);; normal behavior
    public void shouldReturnSameSubTaskWhenGetHimById() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getSubTaskByID(subTask1.getID()));
    }

    @Test //SubTask getSubTaskByID(int ID);; empty list
    public void shouldThrowExceptionWhenGetSubTaskByIdAndSubTaskNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //SubTask getSubTaskByID(int ID);; wrong id
    public void shouldThrowExceptionWhenGetSubTaskByIdAndSubTaskIsNull() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getSubTaskByID(5));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //Epic getEpicByID(int ID);; normal behavior
    public void shouldReturnSameEpicWhenGetHimById() {
        taskManager.create(epic1);
        assertEquals(epic1, taskManager.getEpicByID(epic1.getID()));
    }

    @Test //Epic getEpicByID(int ID);; empty list
    public void shouldThrowExceptionWhenGetEpicByIdAndEpicNotCreated() {
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getEpicByID(epic1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //Epic getEpicByID(int ID);; wrong id
    public void shouldThrowExceptionWhenGetEpicByWrongId() {
        taskManager.create(epic1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getEpicByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //boolean deleteTaskByID(int ID); normal behavior
    public void shouldThrowExceptionWhenTaskDeleted() {
        taskManager.create(task1);
        taskManager.deleteTaskByID(task1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getTaskByID(task1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //boolean deleteTaskByID(int ID); wrong id
    public void shouldThrowExceptionWhenDeleteTaskByWrongId() {
        taskManager.create(task1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.deleteTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //boolean deleteSubTaskByID(int ID); normal behavior
    public void shouldThrowExceptionWhenSubTaskDeleted() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.deleteSubTaskByID(subTask1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //boolean deleteSubTaskByID(int ID); wrong id
    public void shouldThrowExceptionWhenDeleteSubTaskByWrongId() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.deleteTaskByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //boolean deleteSubTaskByID(int ID) check epic status
    public void shouldReturnEpicStatusNewWhenOneOfHisSubTasksWithDoneDeleted() {
        taskManager.create(epic1);
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteSubTaskByID(subTask2.getID());
        assertEquals(Status.NEW, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test //boolean deleteSubTaskByID(int ID) check epic status
    public void shouldReturnEpicStatusDoneWhenHisSubTasksWithNewDeleted() {
        taskManager.create(epic1);
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteSubTaskByID(subTask1.getID());
        assertEquals(Status.DONE, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test //boolean deleteSubTaskByID(int ID) check epic status
    public void shouldReturnEpicStatusDoneWhenHisSubTasksWithInProgressDeleted() {
        taskManager.create(epic1);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.create(subTask1);
        taskManager.create(subTask2);
        taskManager.deleteSubTaskByID(subTask2.getID());
        assertEquals(Status.DONE, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test //boolean deleteEpicByID(int ID); normal behavior
    public void shouldThrowExceptionWhenEpicDeleted() {
        taskManager.create(epic1);
        taskManager.deleteEpicByID(epic1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.getEpicByID(epic1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //boolean deleteEpicByID(int ID); wrong id
    public void shouldThrowExceptionWhenDeleteEpicByWrongId() {
        taskManager.create(epic1);
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.deleteEpicByID(55555));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

    @Test //boolean deleteEpicByID(int ID); additional test
    public void shouldThrowExceptionWhenEpicDeletedAndGetHisSubTask() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        taskManager.deleteEpicByID(epic1.getID());
        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class,
                                        () -> taskManager.getSubTaskByID(subTask1.getID()));
        assertEquals("\nERROR -> [объект с указанным ID не найден]", exception.getMessage());
    }

}
















//    @Test
//    public void deleteAllSubTasksTest() {
//
//        System.out.println("ПЕРВОЕ ДОБАВЛЕНИЕ");
//        epic1 = new Epic("Эпик1", "-");
//        backedManager.create(epic1);
//        subTask1 = new SubTask("Таск1", "-", 1);
//        backedManager.create(subTask1);
//
//        subTask2 = new SubTask("Таск2", "-", 1);
//        backedManager.create(subTask2);
//
//        System.out.println(backedManager.getPrioritizedTasks());
//        System.out.println("ПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ БЕЗ ПЕРЕСЕЧЕНИЯ");
//        subTask1 = new SubTask(
//                subTask1.getID(),
//                subTask1.getName(),
//                subTask1.getDescription(),1,
//                2022, 1, 1, 2, 0,1);
//
//        backedManager.update(subTask1);
//
//        System.out.println(backedManager.getPrioritizedTasks());
//
//        System.out.println("ПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ КЛЮЧЕЙ");
//        subTask2 = new SubTask(
//                subTask2.getID(),
//                subTask2.getName(),
//                subTask2.getDescription(), 1,
//                2022, 1, 1, 2, 0,3);
//        System.out.println(subTask2.getName() + " Интервал: " + subTask2.getStartTime().toLocalTime() + " - " + subTask2.getEndTime().toLocalTime());
//        backedManager.update(subTask2);
//
//        System.out.println(backedManager.getPrioritizedTasks());
//
//        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ ИНТЕРВАЛОВ");
//        subTask2 = new SubTask(
//                subTask2.getID(),
//                subTask2.getName(),
//                subTask2.getDescription(), 1,
//                2022, 1, 1, 1, 59,3);
//        System.out.println(subTask2.getName() + " Интервал: " + subTask2.getStartTime().toLocalTime() + " - " + subTask2.getEndTime().toLocalTime());
//        backedManager.update(subTask2);
//        System.out.println(backedManager.getPrioritizedTasks());
//
//        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ БЕЗ ПЕРЕСЕЧЕНИЯ");
//        subTask2 = new SubTask(
//                subTask2.getID(),
//                subTask2.getName(),
//                subTask2.getDescription(), 1,
//                2022, 1, 1, 3, 0,1);
//        System.out.println(subTask2.getName() + " Интервал: " + subTask2.getStartTime().toLocalTime() + " - " + subTask2.getEndTime().toLocalTime());
//        backedManager.update(subTask2);
//        System.out.println(backedManager.getPrioritizedTasks());
//
//        System.out.println("\nПРОВЕРЯЕМ ИЗМЕНЕНИЕ ВРЕМЕНИ С ПЕРЕСЕЧЕНИЕМ");
//        task2 = new SubTask(
//                subTask2.getID(),
//                subTask2.getName(),
//                subTask2.getDescription(), 1,
//                2022, 1, 1, 1, 59,1);
//        System.out.println(subTask2.getName() + " Интервал: " + subTask2.getStartTime().toLocalTime() + " - " + subTask2.getEndTime().toLocalTime());
//        backedManager.update(subTask2);
//        System.out.println(backedManager.getPrioritizedTasks());
//
//        task3 = new Task("Таск3", "-");
//        backedManager.create(task3);
//        System.out.println(backedManager.getPrioritizedTasks());
//        task3 = new Task(
//                task3.getID(),
//                task3.getName(),
//                task3.getDescription(),
//                2022, 1, 1, 2,30,1);
//        backedManager.update(task3);
//        System.out.println(backedManager.getPrioritizedTasks());
//    }
