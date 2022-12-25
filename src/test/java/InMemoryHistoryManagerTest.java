import org.junit.jupiter.api.*;
import ru.yandex.practicum.managers.HistoryManager;
import ru.yandex.practicum.managers.InMemoryHistoryManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class InMemoryHistoryManagerTest {
    HistoryManager manager;
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
        manager = Managers.getDefaultHistory();
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

    //void add(Task task)
    @Test
    public void returnThrowExceptionWhenIncomingTaskIsNull() {
        manager.add(task1);

    }


}
