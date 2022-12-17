import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.TimeValueException;
import ru.yandex.practicum.utils.TimeConverter;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    public void shouldReturnNullWhenEpicNotHaveSubtasks(){
        Epic epic = new Epic("Имя", "Описание");
        assertNull(epic.setDuration());
    }

    @Test
    public void shouldReturnNotNullWhenEpicHaveSubtasks(){
        Epic epic = new Epic(1,"Имя", "Описание");
        SubTask subTask = new SubTask(2, "Имя", "Описание", 1, 2022, 12,18,15, 0, 5);
        assertEquals(5, epic.getDuration());
    }
}
