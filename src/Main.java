import ru.yandex.practicum.managers.HistoryManager;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.managers.TaskManager;
import ru.yandex.practicum.tasks.*;

public class Main {
    public static void main(String[] args) {

        TaskManager tm = Managers.getDefault();
        HistoryManager hm = Managers.getDefaultHistory();

        Task task = new Task("Таск", "-");
        Task task1 = new Task("Таск", "-");
        Task task2 = new Task("Таск", "-");
        Task task3 = new Task("Таск", "-");
        Task task4 = new Task("Таск", "-");

        tm.create(task);
        tm.create(task1);
        tm.create(task2);
        tm.create(task3);
        tm.create(task4);

        Epic epic = new Epic("Эпик", "-");
        Epic epic1 = new Epic("Эпик", "-");
        Epic epic2 = new Epic("Эпик", "-");
        Epic epic3 = new Epic("Эпик", "-");
        Epic epic4 = new Epic("Эпик", "-");

        tm.create(epic);
        tm.create(epic1);
        tm.create(epic2);
        tm.create(epic3);
        tm.create(epic4);
        tm.create(epic4);

        SubTask subTask = new SubTask("Сабтаск", "-", epic.getID());
        SubTask subTask1 = new SubTask("Сабтаск", "-", epic1.getID());
        SubTask subTask2 = new SubTask("Сабтаск", "-", epic2.getID());
        SubTask subTask3 = new SubTask("Сабтаск", "-", epic3.getID());
        SubTask subTask4 = new SubTask("Сабтаск", "-", epic.getID());

        tm.create(subTask);
        tm.create(subTask1);
        tm.create(subTask2);
        tm.create(subTask3);
        tm.create(subTask4);

        tm.getTaskByID(task3.getID());
        tm.getSubTaskByID(subTask2.getID());
        tm.getSubTaskByID(subTask2.getID());
        tm.getEpicByID(epic1.getID());
        tm.getEpicByID(epic1.getID());
        tm.getSubTaskByID(subTask2.getID());
        tm.getSubTaskByID(subTask2.getID());
        tm.getSubTaskByID(subTask2.getID());
        tm.getTaskByID(task4.getID());
        tm.getSubTaskByID(subTask2.getID());
        tm.getSubTaskByID(subTask2.getID());
        tm.getTaskByID(task3.getID());

        System.out.println(hm.getHistory().toString());
    }
}