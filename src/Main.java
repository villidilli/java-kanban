import ru.yandex.practicum.managers.*;
import ru.yandex.practicum.tasks.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n*** Проверка работоспособности InMemoryManagers ***\n");
        TaskManager tm = Managers.getDefault();

        Task task1 = new Task("Таск1", "-"); //1
        tm.create(task1);
        Task task2 = new Task("Таск2", "-"); //2
        tm.create(task2);
        Epic epic1 = new Epic("Эпик1", "-"); //3
        tm.create(epic1);
        SubTask subTask1 = new SubTask("Саб1", "-", 3); //4
        tm.create(subTask1);
        SubTask subTask2 = new SubTask("Саб2", "-", 3); //5
        tm.create(subTask2);
        SubTask subTask3 = new SubTask("Саб3", "-",3); //6
        tm.create(subTask3);

        System.out.println("\nПроверяем порядок истории (без дублей)");
        tm.getTaskByID(task2.getID());
        tm.getSubTaskByID(subTask2.getID());
        tm.getSubTaskByID(subTask3.getID());
        tm.getEpicByID(epic1.getID());
        System.out.println("\nОжидаем порядок id 2 - 5 - 6 - 3");
        System.out.println(tm.getHistory());

        System.out.println("\nПроверяем порядок истории (с дублями)");
        tm.getTaskByID(2);
        tm.getSubTaskByID(5);
        tm.getSubTaskByID(6);
        tm.getEpicByID(3);
        tm.getTaskByID(1);
        tm.getTaskByID(2);
        tm.getSubTaskByID(6);
        tm.getSubTaskByID(5);
        tm.getEpicByID(3);
        tm.getTaskByID(2);
        System.out.println("\nОжидаем порядок id 1 - 6 - 5 - 3 - 2");
        System.out.println(tm.getHistory());

        System.out.println("\nПроверяем удаление головы");
        tm.deleteTaskByID(1);
        System.out.println(tm.getHistory());

        System.out.println("\nПроверяем удаление хвоста");
        tm.deleteTaskByID(2);
        System.out.println(tm.getHistory());

        System.out.println("\nПроверяем удаление подзадачи");
        tm.deleteSubTaskByID(5);
        System.out.println(tm.getHistory());

        System.out.println("\nПроверяем удаление эпика'");
        tm.deleteEpicByID(3);

        System.out.println(tm.getHistory());

        System.out.println("\n*** Проверка работоспособности FileBackedManagers ***\n");
        FileBackedTasksManager f = Managers.getDefaultFileBacked();

        Task taskF1 = new Task("ТаскФ1", "-"); //1
        f.create(taskF1);
        Task taskF2 = new Task("ТаскФ2", "-"); //2
        f.create(taskF2);
        Epic epicF1 = new Epic("ЭпикФ1", "-"); //3
        f.create(epicF1);
        SubTask subTaskF1 = new SubTask("СабФ1", "-", 3); //4
        f.create(subTaskF1);
        SubTask subTaskF2 = new SubTask("СабФ2", "-", 3); //5
        f.create(subTaskF2);
        SubTask subTaskF3 = new SubTask("СабФ3", "-",3); //6
        f.create(subTaskF3);

        System.out.println("\nПроверяем порядок истории (без дублей) FileBackedManagers");
        f.getTaskByID(taskF2.getID());
        f.getSubTaskByID(subTaskF2.getID());
        f.getSubTaskByID(subTaskF3.getID());
        f.getEpicByID(epicF1.getID());
        System.out.println("\nОжидаем порядок id 2 - 5 - 6 - 3");
        System.out.println(f.getHistory());
    }
}