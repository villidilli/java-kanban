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


    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("Таск1", "-", 2022, 1, 1, 0,0,1);
        task2 = new Task("Таск2", "-");
        epic1 = new Epic("Эпик1", "-");
        epic2 = new Epic("Эпик2", "-");
        subTask1 = new SubTask("СабТаск1", "-", 1,
                2022, 2, 2, 0,0,1);
        subTask2 = new SubTask("СабТаск2", "-", 1,
                2022, 2, 2, 1,0,1);
    }

    @Test // boolean create(Task newTask); standart behavior
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

    @Test // boolean create(Task newSubTask);
    public void shouldReturnSameSubTaskAfterCreate() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(subTask1, taskManager.getSubTaskByID(subTask1.getID()));
    }

    @Test // boolean create(Task newSubTask);
    public void shouldThrowExceptionWhenSubTaskNull() {
        subTask1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.create(subTask1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test // boolean create(Task newSubTask);
    public void shouldReturnEpicStatusNewWhenSubTaskCreated() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(Status.NEW, taskManager.getEpicByID(subTask1.getParentEpicID()).getStatus());
    }

    @Test //boolean create(Epic newEpic);
    public void shouldReturnSameEpicAfterCreate() {
        taskManager.create(epic1);
        assertEquals(epic1, taskManager.getEpicByID(epic1.getID()));
    }

    @Test // boolean create(Epic newEpic);
    public void shouldThrowExceptionWhenEpicNull() {
        epic1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.create(epic1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
    }

    @Test // boolean create(Epic newEpic);
    public void shouldReturnEpicStatusNewWhenEpicCreated() {
        taskManager.create(epic1);
        assertEquals(Status.NEW, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test // boolean create(Epic newEpic);
    public void shouldReturnEpicStatusNewWhenEpicCreatedAndHisSubTaskStatusNew() {
        taskManager.create(epic1);
        taskManager.create(subTask1);
        assertEquals(Status.NEW, taskManager.getEpicByID(epic1.getID()).getStatus());
    }

    @Test // boolean update(Task newTask);
    public void shouldReturnUpdateFieldWhenTaskWasUpdate() {
        taskManager.create(task1);
        String newName = "Updated name";
        task1 = new Task(task1.getID(), newName, task1.getDescription());
        taskManager.update(task1);
        assertEquals(newName, taskManager.getTaskByID(task1.getID()).getName());
    }

    @Test // boolean update(Task newTask);
    public void shouldThrowExceptionBeforeUpdateWhenTaskNull() {
        taskManager.create(task1);
        task1 = null;
        ManagerNotFoundException actualException = assertThrows(ManagerNotFoundException.class,
                                                                () -> taskManager.update(task1));
        assertEquals("\nERROR -> [объект не передан]", actualException.getMessage());
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
}