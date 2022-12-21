package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;
import ru.yandex.practicum.utils.TaskConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FileBackedTasksManager extends InMemoryTaskManager {
	private final File backupfile;

	public FileBackedTasksManager(File file) {
		backupfile = file;
	}

	public static FileBackedTasksManager loadFromFile(File file) {
		final FileBackedTasksManager backedManager = new FileBackedTasksManager(file);
		final HistoryManager histManager = backedManager.historyManager;
		//считали из файла все строки
		String[] rows = readFile(file);
		//начинаем с 1 чтобы пропустить шапку
		List<Integer> history = Collections.emptyList();
		for (int i = 1; i < rows.length; i++) {
			//если строка пустая, значит следующая строка истории
			String line = rows[i];
			Task task;
			if (line.isBlank()) {
				//прибавили к индексу 1, чтобы получить следующую строку (историю)
				//сконвертировали строку истории в список ID
				line = rows[i + 1];
				history = TaskConverter.historyFromString(line);
				break;
			}
			//если строка не пустая значит мы работаем со строками-задачами
			task = TaskConverter.taskFromString(line);
			backedManager.updateGeneratorID(task);
			backedManager.addTask(task);
		}
		//ищет задачу в мапах по ID и записываем в историю
		history.forEach(ID -> histManager.add(backedManager.getTask(ID)));
		return backedManager;
	}

	private static String[] readFile(File file) {
		String fileData;
		String[] rows;
		//вначале считали файл целиком в String, потом рассплитили по строкам в массив
		try {
			fileData = Files.readString(file.toPath());
			rows = fileData.split(TaskConverter.LINE_SEPARATOR);
		} catch (IOException exception) {
			throw new ManagerSaveException("ОТМЕНА ЧТЕНИЯ ИЗ ФАЙЛА -> " + file.getName().toUpperCase());
		}
		return rows;
	}

	private boolean addTask(Task task) {
		if (task == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}
		int ID = task.getID();
		switch (task.getTaskType()) {
			case TASK:
				tasks.put(ID, task);
				break;
			case SUBTASK:
				subTasks.put(ID, (SubTask) task);
				addSubTaskToEpic((SubTask) task);
				break;
			case EPIC:
				epics.put(ID, (Epic) task);
		}
		return true;
	}

	private boolean addSubTaskToEpic(SubTask subTask) {
		if (subTask == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}
		Epic epic = (Epic) getTask(subTask.getParentEpicID());
		epic.getEpicSubTasks().put(subTask.getID(), subTask);
		return true;
	}

	private Task getTask(int ID) {
		Task task = tasks.get(ID);
		if (task == null) {
			task = subTasks.get(ID);
		}
		if (task == null) {
			task = epics.get(ID);
		}
		return task;
	}

	private void updateGeneratorID(Task task) {
		if (task == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}
		if (generatorID <= task.getID()) {
			generatorID = task.getID() + 1;
		}
	}

	private void writeHistoryToFile(BufferedWriter bufferedWriter, HistoryManager historyManager) {
		//получили историю (ID) в строчном представлении
		String history = TaskConverter.historyToString(historyManager);
		try {
			bufferedWriter.write(history);
		} catch (IOException exception) {
			throw new ManagerSaveException("ОТМЕНА ЗАПИСИ В ФАЙЛ -> " + backupfile.getName().toUpperCase());
		}
	}

	private void writeTaskToFile(BufferedWriter bufferedWriter, Map<Integer, ? extends Task> list) {
		//принимаем одну из мап с задачами и перебираем значения (объекты)
		list.values().forEach(task -> {
			try {
				//пишем в файл полученную в строчном представлении задачу
				bufferedWriter.write(TaskConverter.taskToString(task));
				bufferedWriter.newLine();
			} catch (IOException exception) {
				throw new ManagerSaveException("ОТМЕНА ЗАПИСИ В ФАЙЛ -> " + backupfile.getName().toUpperCase());
			}
		});
	}

	private String getHeaderTasks() {
		return "id,type,name,status,description,epic,start,duration";
	}

	private boolean save() {
		try (BufferedWriter bufferedWriter =
					 new BufferedWriter(new FileWriter(backupfile, StandardCharsets.UTF_8))) {
			//записали шапку
			bufferedWriter.write(getHeaderTasks());
			bufferedWriter.newLine();
			writeTaskToFile(bufferedWriter, tasks);
			writeTaskToFile(bufferedWriter, epics);
			writeTaskToFile(bufferedWriter, subTasks);
			bufferedWriter.newLine();
			writeHistoryToFile(bufferedWriter, historyManager);
			bufferedWriter.flush();
		} catch (IOException exception) {
			throw new ManagerSaveException(
					"Ошибка -> Не удалось сохранить объекты в файл" + backupfile.getName().toUpperCase());
		}
		return true;
	}

	@Override
	public boolean create(Task newTask) {
		super.create(newTask);
		return save();
	}

	@Override
	public boolean create(SubTask newSubTask) {
		super.create(newSubTask);
		return save();
	}

	@Override
	public boolean create(Epic newEpic) {
		super.create(newEpic);
		return save();
	}

	@Override
	public boolean update(Task newTask) {
		super.update(newTask);
		return save();
	}

	@Override
	public boolean update(SubTask newSubTask) {
		super.update(newSubTask);
		return save();
	}

	@Override
	public boolean update(Epic newEpic) {
		super.update(newEpic);
		return save();
	}

	@Override
	public boolean deleteAllTasks() {
		super.deleteAllTasks();
		return save();
	}

	@Override
	public boolean deleteAllSubTasks() {
		super.deleteAllSubTasks();
		return save();
	}

	@Override
	public boolean deleteAllEpics() {
		super.deleteAllEpics();
		return save();
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
	public boolean deleteTaskByID(int ID) {
		super.deleteTaskByID(ID);
		return save();
	}

	@Override
	public boolean deleteSubTaskByID(int ID) {
		super.deleteSubTaskByID(ID);
		return save();
	}

	@Override
	public boolean deleteEpicByID(int ID) {
		super.deleteEpicByID(ID);
		return save();
	}
}