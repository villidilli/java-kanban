import org.junit.jupiter.api.*;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {
    TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }


}
