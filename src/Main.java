import domain.*;
import manager.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();


        System.out.println("----------------");
        Task writeLetter = new Task("Купить хлеб", "не дороже 100 р");
        manager.create(writeLetter);
        Task repairClock = new Task("Починить часы", "-");
        manager.create(repairClock);
        System.out.println(manager.getAllTasks().toString());
        System.out.println("----------------");
        SubTask buyBricks = new SubTask("Купить кирпичи", "-", 1);
        manager.create(buyBricks);
        System.out.println("----------------");
        Epic buildHouse = new Epic("Построить дом", "-");
        manager.create(buildHouse);
        System.out.println(manager.getAllEpics());
        System.out.println("----------------");
        buyBricks = new SubTask("Купить кирпичи", "-", 3);
        manager.create(buyBricks);
        System.out.println(manager.getAllSubTasks());;
        System.out.println("----------------");
        buyBricks = new SubTask(4, "Купить пеноблоки", "", 3);
        manager.update(buyBricks);
        System.out.println(manager.getAllSubTasks());
        System.out.println("----------------");
        SubTask buildWalls = new SubTask("Построить стены", "", 3);
        manager.create(buildWalls);
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllSubTasksByEpic(3));
        System.out.println("----------------");
        Epic learnJava = new Epic("Выучить Java", "на ЯП");
        manager.create(learnJava);
        SubTask completeHW3 = new SubTask("Сдать ТЗ-3", "-", 6);
        manager.create(completeHW3);
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllSubTasksByEpic(6));
        manager.deleteSubTaskByID(7);
        System.out.println(manager.getAllSubTasksByEpic(6));
        buildWalls.setStatus(Status.DONE);
        manager.update(buildWalls);
        System.out.println(manager.getEpicByID(3));

    }
}
