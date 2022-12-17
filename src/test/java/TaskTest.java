import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TimeValueException;
import ru.yandex.practicum.utils.TimeConverter;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskTest {
    Task task;

    @Test
    public void shouldThrowExceptionWhenDurationNull(){
        task = new Task("Таск", "-");
        TimeValueException e = assertThrows(TimeValueException.class, () -> task.getDuration());
        assertEquals("Продолжительность недоступна. Значение = null или отрицательное", e.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDurationNegative(){
        task = new Task(1, "Таск", "-", 2022, 12, 17, 3, 4, -15);
        TimeValueException e = assertThrows(TimeValueException.class, () -> task.getDuration());
        assertEquals("Продолжительность недоступна. Значение = null или отрицательное", e.getMessage());
    }

    @Test
    public void shouldReturnEndTimeWhenInputCorrectValues(){
        task = new Task(1, "Таск", "-", 2022, 01, 1, 0, 0, 10);
        String expectedEndTime = "01-01-2022 | 00:10 | +03:00";
        String actualEndTime = TimeConverter.dateTimeToString(task.getEndTime());
        assertEquals(expectedEndTime, actualEndTime);
    }
}
