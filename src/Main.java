import ru.yandex.practicum.managers.*;
import ru.yandex.practicum.tasks.*;

public class Main {
    public static void main(String[] args) {

        TaskManager tm = Managers.getDefault();
        FileBackedTasksManager f = new FileBackedTasksManager();
        Task task1 = new Task("Таск1", "-"); //1
        f.create(task1);
        Task task2 = new Task("Таск2", "-"); //2
        f.create(task2);
        Epic epic1 = new Epic("Эпик1", "-"); //3
        f.create(epic1);
        SubTask subTask1 = new SubTask("Саб1", "-", 3); //4
        f.create(subTask1);
        SubTask subTask2 = new SubTask("Саб2", "-", 3); //5
        f.create(subTask2);
        SubTask subTask3 = new SubTask("Саб3", "Описание саб3",3); //6
        f.create(subTask3);

        System.out.println("\nПроверяем порядок истории (без дублей)");
        f.getTaskByID(task2.getID());
        f.getSubTaskByID(subTask2.getID());
        f.getSubTaskByID(subTask3.getID());
        f.getEpicByID(epic1.getID());
        System.out.println("\nОжидаем порядок id 2 - 5 - 6 - 3");
        System.out.println(f.getHistory());

        System.out.println("\nПроверяем порядок истории (с дублями)");
        f.getTaskByID(2);
        f.getSubTaskByID(5);
        f.getSubTaskByID(6);
        f.getEpicByID(3);
        f.getTaskByID(1);
        f.getTaskByID(2);
        f.getSubTaskByID(6);
        f.getSubTaskByID(5);
        f.getEpicByID(3);
        f.getTaskByID(2);
        System.out.println("\nОжидаем порядок id 1 - 6 - 5 - 3 - 2");
        System.out.println(f.getHistory());
        f.save();


  /*      System.out.println("\nПроверяем удаление головы");
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
 */


    }
}