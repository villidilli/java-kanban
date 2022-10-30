import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.taskmanager.InMemoryTaskManager;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Epic epic = new Epic("Эпик", "-");
        manager.create(epic);

        SubTask subTask1 = new SubTask("Сабтаск", "-", 1);
        manager.create(subTask1);
        SubTask subTask2 = new SubTask("Сабтаск2", "-", 1);
        manager.create(subTask2);

        subTask1.setStatus(Status.DONE);
        manager.update(subTask1);
        subTask2.setStatus(Status.DONE);
        manager.update(subTask2);
        System.out.println(manager.getEpicByID(1));
        manager.deleteAllSubTasks();
        System.out.println(manager.getEpicByID(1));

        System.out.println(manager.getHistory());
    }
}