import org.junit.jupiter.api.*;
import ru.yandex.practicum.managers.HistoryManager;
import ru.yandex.practicum.managers.InMemoryHistoryManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;

public class InMemoryHistoryManagerTest {
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    SubTask subTask4;
    @BeforeEach
    public void beforeEach() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        task1 = new Task("Таск1", "-");
        task2 = new Task("Таск2", "-");
        epic1 = new Epic("Эпик1", "-");
        epic2 = new Epic("Эпик2", "-");
        subTask1 = new SubTask("СабТаск1", "-", 1);
        subTask2 = new SubTask("СабТаск2", "-", 1);
        subTask3 = new SubTask("СабТаск3", "-", 1);
        subTask4 = new SubTask("СабТаск4", "-", 1);
    }


}
