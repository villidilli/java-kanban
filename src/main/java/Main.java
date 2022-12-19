import ru.yandex.practicum.managers.*;
import ru.yandex.practicum.tasks.*;

import java.io.File;

public class Main {
	public static void main(String[] args) {
//		System.out.println("*** ПРОВЕРКА РАБОТОСПОСОБНОСТИ FileBackedManagers ***");
//		System.out.println("*****************************************************");
//		System.out.println("*** ИМИТИРУЕМ ПЕРВЫЙ ЗАПУСК ПРОГРАММЫ ***");
//		System.out.println("*** Создаем задачи и наполняем историю ***\n");
//		FileBackedTasksManager f = Managers.getDefaultFileBacked();
//		Task task1 = new Task("Таск1", "-"); //1
//		f.create(task1);
//		Epic epic1 = new Epic("Эпик1", "-"); //2
//		f.create(epic1);
//		SubTask subTask1 = new SubTask("Саб1", "-", epic1.getID(), 2022, 12, 19,1,34,15); //3
//		f.create(subTask1);
//		SubTask subTask2 = new SubTask("Саб2", "-", epic1.getID(), 2022, 12,1, 5,45,45); //4
//		f.create(subTask2);
//		SubTask subTask3 = new SubTask("Саб3", "-", epic1.getID(),2021, 1,1,0,0,30); //5
//		f.create(subTask3);
//
//		f.getSubTaskByID(subTask2.getID()); //4
//		f.getSubTaskByID(subTask3.getID()); //5
//		f.getSubTaskByID(subTask1.getID()); //3
//		f.getEpicByID(epic1.getID()); //2
//
//		System.out.printf("\nИТОГО СОЗДАНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
//				f.getAllTasks().size(), f.getAllSubTasks().size(), f.getAllEpics().size());
//		System.out.println("\nИСТОРИЯ: " + f.getHistory());
//
//		System.out.println("\n*** Программа завершена ***");
//		System.out.println("-----------------------------");
//
//		System.out.println("\n*** ИМИТИРУЕМ ВТОРОЙ ЗАПУСК ПРОГРАММЫ ***");
//		FileBackedTasksManager f2 = Managers.getDefaultFileBacked();
//
//		System.out.printf("\nВОССТАНОВЛЕНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
//				f2.getAllTasks().size(), f2.getAllSubTasks().size(), f2.getAllEpics().size());
//		System.out.println("\nИСТОРИЯ: " + f2.getHistory());
//
//		System.out.println("\n*** Добавляем задачи и дополняем историю ***\n");
//		Task task6 = new Task("Таск6", "-");
//		f2.create(task6);
//		f2.getTaskByID(task6.getID());
//
//		System.out.printf("\nИТОГО СОЗДАНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
//				f2.getAllTasks().size(), f2.getAllSubTasks().size(), f2.getAllEpics().size());
//		System.out.println("\nИСТОРИЯ: " + f2.getHistory());
//
//		System.out.println("\n*** Программа завершена ***");
//		System.out.println("-----------------------------");
//
//		System.out.println("\n*** ИМИТИРУЕМ ТРЕТИЙ ЗАПУСК ПРОГРАММЫ ***");
//		FileBackedTasksManager f3 = Managers.getDefaultFileBacked();
//
//		System.out.printf("\nВОССТАНОВЛЕНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
//				f3.getAllTasks().size(), f3.getAllSubTasks().size(), f3.getAllEpics().size());
//		System.out.println("\nИСТОРИЯ: " + f3.getHistory());
//		Epic epic7 = new Epic("Эпик7", "-");
//
//		System.out.println("\n*** Добавляем задачи и дополняем историю ***\n");
//		f3.create(epic7);
//		f3.getEpicByID(epic7.getID());
//		System.out.printf("\nИТОГО СОЗДАНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
//				f3.getAllTasks().size(), f3.getAllSubTasks().size(), f3.getAllEpics().size());
//		System.out.println("\nИСТОРИЯ: " + f3.getHistory());
//
//		System.out.println("\n*** Удаляем задачу ***\n");
//		f3.deleteEpicByID(epic7.getID());
//		System.out.printf("\nИТОГО СОЗДАНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
//				f3.getAllTasks().size(), f3.getAllSubTasks().size(), f3.getAllEpics().size());
//		System.out.println("\nИСТОРИЯ: " + f3.getHistory());
//
//		System.out.println("\n*** Удаляем все задачи одного типа ***\n");
//		f3.deleteAllEpics();
//		System.out.printf("\nИТОГО СОЗДАНО: ТАСК = %d, САБ = %d, ЭПИК = %d",
//				f3.getAllTasks().size(), f3.getAllSubTasks().size(), f3.getAllEpics().size());
//		System.out.println("\nИСТОРИЯ: " + f3.getHistory());
//
//		System.out.println("\n*** Программа завершена ***");
//		System.out.println("-----------------------------");
		System.out.println("***** ПЕРВЫЙ ЗАПУСК *****\n");
		FileBackedTasksManager f = Managers.getDefaultFileBacked();

		System.out.println("ВОССТАНОВЛЕНО ИЗ ФАЙЛА: ");
		System.out.println("ТАСК [" + f.getAllTasks().size() +
							"] САБ [" + f.getAllSubTasks().size() +
							"] ЭПИК [" + f.getAllEpics().size() + "]\n");

		System.out.println("Создаем задачи");
		Task task1 = new Task("Таск1", "-", 2022, 1, 1, 1, 0, 5);
		f.create(task1); //1

		Epic epic1 = new Epic("Эпик", "-");
		f.create(epic1); //2

		SubTask sub1 = new SubTask("Саб1", "-", 2, 2022, 3, 12, 20, 0, 10);
		f.create(sub1); //3

		Task task2 = new Task("Таск2", "-");
		f.create(task2);

		SubTask sub2 = new SubTask("Саб2", "-", 2);
		f.create(sub2);
	}
}