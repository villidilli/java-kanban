import ru.yandex.practicum.managers.*;
import ru.yandex.practicum.tasks.*;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		TaskManager manager = new FileBackedTasksManager(new File("src/main/resources/Backup.csv"));
//		TaskManager manager = Managers.getDefaultFileBacked();
		Task task1 = new Task("task1", "", 10000,1,1,0,0,1);
		Task task2 = new Task("task2","", 2022, 1,1,0,0,1);
		Task task3 = new Task("task3","");
		Task task4 = new Task("task4","", 2021, 12,31,23,59,0);
		Task task5 = new Task("task5","");
		Task task6 = new Task(task5.getID(),"task6","");

		manager.create(task1);
		manager.create(task2);
		manager.create(task3);
		manager.create(task4);
		manager.create(task5);
		task4 = new Task(
				task4.getID(),
				"Новое имя".toUpperCase(),
				task4.getDescription(),
				task4.getStatus(),
				task4.getStartTime(),
				task4.getDuration());
		manager.update(task4);
				task2 = new Task(
				task2.getID(),
				task2.getName(),
				task2.getDescription(),
				task2.getStatus(),
				1500, 6, 15, 12,0,
				task2.getDuration());
		manager.update(task2);
		task6 = new Task(
				5,
				"Имя 6",
				task6.getDescription());
		manager.update(task6);
		task2 = new Task(
				task2.getID(),
				task2.getName(),
				task2.getDescription());
		manager.update(task2);

		task2 = new Task(
				task2.getID(),
				task2.getName(),
				task2.getDescription(),
				15000, 1, 1, 15, 30, 1);
		manager.update(task2);
		task2 = new Task(
				task2.getID(),
				task2.getName(),
				task2.getDescription(),
				2021, 12, 31, 22, 59, 0);
		manager.update(task2);


		System.out.println(manager.getPrioritizedTasks());

		manager.deleteAllTasks();
		System.out.println(manager.getPrioritizedTasks());
	}
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
}