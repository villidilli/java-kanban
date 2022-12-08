import ru.yandex.practicum.managers.*;
import ru.yandex.practicum.tasks.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("*** ПРОВЕРКА РАБОТОСПОСОБНОСТИ FileBackedManagers ***");
        System.out.println("*****************************************************");
        System.out.println("*** ИМИТИРУЕМ ПЕРВЫЙ ЗАПУСК ПРОГРАММЫ ***");
        System.out.println("*** Создаем задачи и наполняем историю (main() FileBackedManager) ***\n");
        FileBackedTasksManager f = new FileBackedTasksManager(
                new File("resources\\Backup.csv"));
        f.main(null);

        System.out.println("\n*** Программа завершена ***");
        System.out.println("-----------------------------");

        System.out.println("\n*** ИМИТИРУЕМ ВТОРОЙ ЗАПУСК ПРОГРАММЫ ***");
        FileBackedTasksManager f2 = Managers.getDefaultFileBacked();

        System.out.printf("\nВОССТАНОВЛЕНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
                f2.getAllTasks().size(), f2.getAllSubTasks().size(), f2.getAllEpics().size());
        System.out.println("\nИСТОРИЯ: " + f2.getHistory());

        System.out.println("\n*** Добавляем задачи и дополняем историю ***\n");
        Task task6 = new Task("Таск6", "-");
        f2.create(task6);
        f2.getTaskByID(task6.getID());

        System.out.printf("\nСОЗДАНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
                f2.getAllTasks().size(), f2.getAllSubTasks().size(), f2.getAllEpics().size());
        System.out.println("\nИСТОРИЯ: " + f2.getHistory());

        System.out.println("\n*** Программа завершена ***");
        System.out.println("-----------------------------");

        System.out.println("\n*** ИМИТИРУЕМ ТРЕТИЙ ЗАПУСК ПРОГРАММЫ ***");
        FileBackedTasksManager f3 = Managers.getDefaultFileBacked();

        System.out.printf("\nВОССТАНОВЛЕНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
                f3.getAllTasks().size(), f3.getAllSubTasks().size(), f3.getAllEpics().size());
        System.out.println("\nИСТОРИЯ: " + f3.getHistory());
        Epic epic7 = new Epic("Эпик7", "-");

        System.out.println("\n*** Добавляем задачи и дополняем историю ***\n");
        f3.create(epic7);
        f3.getEpicByID(epic7.getID());

        System.out.printf("\nСОЗДАНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
                f3.getAllTasks().size(), f3.getAllSubTasks().size(), f3.getAllEpics().size());
        System.out.println("\nИСТОРИЯ: " + f3.getHistory());

        System.out.println("\n*** Программа завершена ***");
        System.out.println("-----------------------------");
    }
}