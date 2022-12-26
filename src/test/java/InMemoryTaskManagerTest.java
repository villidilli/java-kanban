import org.junit.jupiter.api.BeforeEach;

import ru.yandex.practicum.managers.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest <InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        manager = new InMemoryTaskManager();
    }
}