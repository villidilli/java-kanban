import domain.*;
import manager.TaskManager;

import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
//        Task buyBread = new Task("Купить хлеб" ,"Бородинский"); // первое вхождение
//        manager.create(buyBread);
//
//        buyBread = new Task(1, "Купить батон", "два батона", Status.DONE);
//        manager.update(buyBread);
//        System.out.println(buyBread.toString());

        Epic buildHouse = new Epic("Построить дом", "Квадратов этак на 500"); // первое вхождение
        manager.create(buildHouse);
        System.out.println(buildHouse.toString());

        SubTask foundation = new SubTask("Залить фундамент", "Нанять рабочих", 1); // первое вхождение
        manager.create(foundation);
        System.out.println(foundation.toString());

        foundation = new SubTask(2, "Залить фундамент", "Нанять рабочих", Status.DONE,1);
        manager.update(foundation);
        SubTask walls = new SubTask("Выложить стены", "из красного кирпича", 1); // первое вхождение
        manager.create(walls);
        walls = new SubTask(3,"Выложить стены", "из красного кирпича",Status.DONE, 1);
        manager.update(walls);
        System.out.println(foundation.toString());
        System.out.println(walls.toString());
        System.out.println(buildHouse.toString());



//        SubTask foundation = new SubTask("Залить фундамент", "Нанять рабочих", 1); // первое вхождение
//        manager.create(foundation);



        //        Epic buildHouse = new Epic("Построить дом", "Квардратов этам на 500"); // первое вхождение
//        manager.create(buildHouse);
//        SubTask foundation = new SubTask("Залить фундамент", "Нанять рабочих", 2); // первое вхождение
//        manager.create(foundation);
//        SubTask walls = new SubTask("Выложить стены", "из красного кирпича", 2); // первое вхождение
//        manager.create(walls);

    }
}
