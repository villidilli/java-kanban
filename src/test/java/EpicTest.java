import org.junit.jupiter.api.*;

import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;


import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EpicTest {
    Epic epic1;
    SubTask subTask1;
    SubTask subTask2;
    TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        epic1 = new Epic(1, "Эпик1", "-");
        subTask1 = new SubTask(2, "Саб1", "-", 1, 2022, 12, 18, 1,30, 30);
        subTask2 = new SubTask(3, "Саб2", "-", 1, 2022, 12, 18, 0, 30, 60);
        manager = Managers.getDefault();
        manager.create(epic1);
        manager.create(subTask1);
        manager.create(subTask2);
    }
    @Test
    public void shouldReturnEpicStartTimeEqualsLeastSubTaskStartTime() {
        ZonedDateTime expectedStartTime = ZonedDateTime.of(2022, 12,18,0, 30, 0,0, ZoneId.systemDefault());
        assertEquals(expectedStartTime, epic1.getStartTime());
    }

    @Test
    public void shouldReturnEpicDurationEqualsSumSubTasksDurations() {
        int expectedDuration = 90;
        assertEquals(expectedDuration, epic1.getDuration());
        System.out.println(subTask1.getDuration());
        System.out.println(subTask2.getDuration());
        System.out.println(epic1.getDuration());
    }
}
