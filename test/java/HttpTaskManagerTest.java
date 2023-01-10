import org.junit.jupiter.api.BeforeAll;
import ru.yandex.practicum.api.Servers;
import ru.yandex.practicum.managers.HttpTaskManager;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    @BeforeAll
    public static void beforeAll() {
        Servers.getKVServer().start();
    }
}
