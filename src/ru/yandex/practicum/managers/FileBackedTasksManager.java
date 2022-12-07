package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.utils.TaskConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
	private final File backupfile;

	public FileBackedTasksManager(File file) {
		backupfile = file;
	}

	public static void main(String[] args) {
		FileBackedTasksManager f = new FileBackedTasksManager(
				new File("D:\\JavaDev\\java-kanban\\resources\\Backup.csv"));

		Task task1 = new Task("Таск1", "-"); //1
		f.create(task1);
		Epic epic1 = new Epic("Эпик1", "-"); //2
		f.create(epic1);
		SubTask subTask1 = new SubTask("Саб1", "-", 2); //3
		f.create(subTask1);
		SubTask subTask2 = new SubTask("Саб2", "-", 2); //4
		f.create(subTask2);
		SubTask subTask3 = new SubTask("Саб3", "-", 2); //5
		f.create(subTask3);

		f.getSubTaskByID(subTask2.getID()); //4
		f.getSubTaskByID(subTask3.getID()); //5
		f.getSubTaskByID(subTask1.getID()); //3
		f.getEpicByID(epic1.getID()); //2

	}

	public static FileBackedTasksManager loadFromFile(File file) {
		final FileBackedTasksManager backedManager = new FileBackedTasksManager(file);
		final HistoryManager histManager = backedManager.historyManager;
		//считали из файла все строки
		String[] rows = readFile(file);
		//начинаем с 1 чтобы пропустить шапку
		for (int i = 1; i < rows.length; i++) {
			//если строка пустая, значит следующая строка истории
			String line = rows[i];
			Task task;
			if (line.isBlank()) {
				//прибавили к индексу 1, чтобы получить следующую строку (историю)
				//сконвертировали строку истории в список ID
				line = rows[++i];
				List<Integer> history = TaskConverter.historyFromString(line);
				//ищет задачу в мапах по ID и записываем в историю
				for (Integer ID : history) {
					task = backedManager.getTask(ID);
					histManager.add(task);
				}
				//если строка не пустая значит мы работаем со строками-задачами
			} else {
				task = TaskConverter.taskFromString(line);
				backedManager.updateGeneratorID(task);
				backedManager.addTask(task);
			}
		}
		return backedManager;
	}

	private void addTask(Task task) {
		int ID = task.getID();
		switch (task.getTaskType()) {
			case TASK:
				tasks.put(ID, task);
				break;
			case SUBTASK:
				subTasks.put(ID, (SubTask) task);
				break;
			case EPIC:
				epics.put(ID, (Epic) task);
		}
	}

	private Task getTask(int ID) {
		Task task = tasks.get(ID);
		if (task == null) {
			task = subTasks.get(ID);
			if (task == null) {
				task = epics.get(ID);
			}
		}
		return task;
	}

	private void updateGeneratorID(Task task){
		if (generatorID < task.getID()) {
			generatorID = task.getID() + 1;
		}
	}

	private static String[] readFile(File file) {
		String fileData;
		String[] rows;
		//вначале считали файл целиком в String, потом рассплитили по строкам в массив
		try {
			fileData = Files.readString(file.toPath());
			rows = fileData.split("\\r?\\n");
		} catch (IOException exception) {
			throw new ManagerSaveException("Ошибка -> Чтение файла невозможно");
		}
		return rows;
	}

	private void writeHistoryToFile(BufferedWriter bufferedWriter, HistoryManager historyManager) {
		//получили историю (ID) в строчном представлении
		String history = TaskConverter.historyToString(historyManager);
		try {
			bufferedWriter.write(history);
		} catch (IOException exception) {
			throw new ManagerSaveException("Ошибка -> Не удалось записать в файл историю");
		}
	}

	private void writeTaskToFile(BufferedWriter bufferedWriter, Map<Integer, ? extends Task> list) {
		//принимаем одну из мап с задачами и перебираем значения (объекты)
		try {
			for (Task task : list.values()) {
				//пишем в файл полученную в строчном представлении задачу
				bufferedWriter.write(TaskConverter.taskToString(task) + "\n");
			}
		} catch (IOException exception) {
			throw new ManagerSaveException("Ошибка -> Не удалось записать данные задачи");
		}
	}

	private String getHeaderTasks() {
		return "id,type,name,status,description,epic\n";
	}

	private void save() {
		try (BufferedWriter bufferedWriter =
					 new BufferedWriter(new FileWriter(backupfile, StandardCharsets.UTF_8))) {
			//записали шапку
			bufferedWriter.write(getHeaderTasks());
			writeTaskToFile(bufferedWriter, tasks);
			writeTaskToFile(bufferedWriter, subTasks);
			writeTaskToFile(bufferedWriter, epics);
			bufferedWriter.newLine();
			writeHistoryToFile(bufferedWriter, historyManager);
			bufferedWriter.flush();
		} catch (IOException exception) {
			throw new ManagerSaveException("Ошибка -> Не удалось записать объекты в файл");
		}
	}

	@Override
	public void create(Task newTask) {
		super.create(newTask);
		save();
	}

	@Override
	public void create(SubTask newSubTask) {
		super.create(newSubTask);
		save();
	}

	@Override
	public void create(Epic newEpic) {
		super.create(newEpic);
		save();
	}

	@Override
	public void update(Task newTask) {
		super.update(newTask);
		save();
	}

	@Override
	public void update(SubTask newSubTask) {
		super.update(newSubTask);
		save();
	}

	@Override
	public void update(Epic newEpic) {
		super.update(newEpic);
		save();
	}

	@Override
	public void deleteAllTasks() {
		super.deleteAllTasks();
		save();
	}

	@Override
	public void deleteAllSubTasks() {
		super.deleteAllSubTasks();
		save();
	}

	@Override
	public void deleteAllEpics() {
		super.deleteAllEpics();
		save();
	}

	@Override
	public Task getTaskByID(int ID) {
		Task task = super.getTaskByID(ID);
		save();
		return task;
	}

	@Override
	public SubTask getSubTaskByID(int ID) {
		SubTask subTask = super.getSubTaskByID(ID);
		save();
		return subTask;
	}

	@Override
	public Epic getEpicByID(int ID) {
		Epic epic = super.getEpicByID(ID);
		save();
		return epic;
	}

	@Override
	public void deleteTaskByID(int ID) {
		super.deleteTaskByID(ID);
		save();
	}

	@Override
	public void deleteSubTaskByID(int ID) {
		super.deleteSubTaskByID(ID);
		save();
	}

	@Override
	public void deleteEpicByID(int ID) {
		super.deleteEpicByID(ID);
		save();
	}
}