import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.managers.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        manager = new InMemoryTaskManager();
    }

    @Test
    public void shouldReturn0sizeWhenAnyTasksNotCreated() {
        assertEquals(0, manager.getAllTasks().size());
        assertEquals(0, manager.getAllSubTasks().size());
        assertEquals(0, manager.getAllSubTasks().size());
    }

    @Test
    public void shouldReturnNotNullWhenAnyTasksNotCreated() {
        assertNotNull(manager.getAllTasks());
        assertNotNull(manager.getAllSubTasks());
        assertNotNull(manager.getAllSubTasks());
    }
}