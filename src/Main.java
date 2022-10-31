import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.tasks.*;

public class Main {
    public static void main(String[] args) {

        Task task = new Task("Таск", "-");
        Task task1 = new Task("Таск", "-");
        Task task2 = new Task("Таск", "-");
        Task task3 = new Task("Таск", "-");
        Task task4 = new Task("Таск", "-");

        Managers.getDefault().create(task);
        Managers.getDefault().create(task1);
        Managers.getDefault().create(task2);
        Managers.getDefault().create(task3);
        Managers.getDefault().create(task4);

        Epic epic = new Epic("Эпик", "-");
        Epic epic1 = new Epic("Эпик", "-");
        Epic epic2 = new Epic("Эпик", "-");
        Epic epic3 = new Epic("Эпик", "-");
        Epic epic4 = new Epic("Эпик", "-");

        Managers.getDefault().create(epic);
        Managers.getDefault().create(epic1);
        Managers.getDefault().create(epic2);
        Managers.getDefault().create(epic3);
        Managers.getDefault().create(epic4);
        Managers.getDefault().create(epic4);

        SubTask subTask = new SubTask("Сабтаск", "-", epic.getID());
        SubTask subTask1 = new SubTask("Сабтаск", "-", epic1.getID());
        SubTask subTask2 = new SubTask("Сабтаск", "-", epic2.getID());
        SubTask subTask3 = new SubTask("Сабтаск", "-", epic3.getID());
        SubTask subTask4 = new SubTask("Сабтаск", "-", epic.getID());

        Managers.getDefault().create(subTask);
        Managers.getDefault().create(subTask1);
        Managers.getDefault().create(subTask2);
        Managers.getDefault().create(subTask3);
        Managers.getDefault().create(subTask4);

        Managers.getDefault().getTaskByID(task3.getID());
        Managers.getDefault().getSubTaskByID(subTask2.getID());
        Managers.getDefault().getSubTaskByID(subTask2.getID());

        System.out.println(Managers.getDefaultHistory().getHistory().toString());
    }
}