import org.junit.jupiter.api.*;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {
    TaskManager manager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    SubTask sub1;
    SubTask sub2;


    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
        task1 = new Task("Таск1", "-", 2022, 12, 31,23,59,1);
        manager.create(task1);
        task2 = new Task("Таск2", "-", 2022, 11, 30, 23, 55 ,1);
        manager.create(task2);
        epic1 = new Epic("Эпик1", "-");
        manager.create(epic1);
        sub1 = new SubTask("Сабтаск1", "-" , epic1.getID());
        manager.create(sub1);
        sub2 = new SubTask("Сабтаск2", "-", epic1.getID(), 2022, 12, 31, 0, 0,1);
        manager.create(sub2);
    }

    @Test
    public void getPrioritezedTasksWhenAllTasksHaveStartTime() {
        List<Task> list = List.of(task1, task2, sub1, sub2);
        List<Task> expectedList = list.stream()
                        .sorted((o1, o2) -> {
                            if (o1.getStartTime().isBefore(o2.getStartTime())) {
                                return -1;
                            }
                            if (o1.getStartTime().isAfter(o2.getStartTime())) {
                                return 1;
                            }
                            return 0;
                        })
                        .collect(Collectors.toList());

        assertArrayEquals(expectedList.toArray(), manager.getPrioritizedTasks().toArray());
        System.out.println(list);
        System.out.println(expectedList);
        System.out.println(manager.getPrioritizedTasks());
    }
}
