import ru.yandex.practicum.managers.HistoryManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.managers.InMemoryTaskManager;

public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Таск", "-");
        taskManager.create(task);

        System.out.println(taskManager.getTaskByID(1));

    }
}