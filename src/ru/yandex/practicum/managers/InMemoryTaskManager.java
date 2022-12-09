package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
	protected final Map<Integer, Task> tasks = new HashMap<>();
	protected final Map<Integer, SubTask> subTasks = new HashMap<>();
	protected final Map<Integer, Epic> epics = new HashMap<>();
	protected final HistoryManager historyManager = Managers.getDefaultHistory();
	protected int generatorID = 1;

	private void reCheckEpicStatus(int epicID) {
		int countDone = 0;
		int countNew = 0;
		HashMap<Integer, SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks();

		if (epicSubTasks.size() == 0) {
			epics.get(epicID).setStatus(Status.NEW);
		} else {
			for (SubTask subTask : epicSubTasks.values()) {
				if (subTask.getStatus() == Status.NEW) {
					countNew++;
				} else if (subTask.getStatus() == Status.DONE) {
					countDone++;
				}
			}
			if (countNew == epicSubTasks.size()) {
				epics.get(epicID).setStatus(Status.NEW);
			} else if (countDone == epicSubTasks.size()) {
				epics.get(epicID).setStatus(Status.DONE);
			} else {
				epics.get(epicID).setStatus(Status.IN_PROGRESS);
			}
		}
	}

	@Override
	public void create(Task newTask) {
		if (newTask == null) {
			System.out.println("[Ошибка] Объект задачи равен *null*");
		} else {
			newTask.setID(generatorID);
			generatorID++;
			tasks.put(newTask.getID(), newTask);
			System.out.println("Задача: [" + newTask.getName() + "] [ID: " + newTask.getID() + "] создана!");
		}
	}

	@Override
	public void create(SubTask newSubTask) {
		if (newSubTask != null) {
			Epic parentEpic = epics.get(newSubTask.getParentEpicID());
			if (parentEpic != null) {
				newSubTask.setID(generatorID);
				subTasks.put(generatorID, newSubTask);
				parentEpic.getEpicSubTasks().put(generatorID, newSubTask);
				reCheckEpicStatus(parentEpic.getID());
				generatorID++;
				System.out.println("Подзадача: [" + newSubTask.getName() + "] [ID: " + newSubTask.getID() + "] [ID эпика: " + newSubTask.getParentEpicID() + "] создана!");
			} else {
				System.out.println("Эпик с ID [" + newSubTask.getParentEpicID() + "] не найден. " + "Подзадача не может быть создана");
			}
		} else {
			System.out.println("[Ошибка] Объект задачи равен *null*");
		}
	}

	@Override
	public void create(Epic newEpic) {
		if (newEpic == null) {
			System.out.println("[Ошибка] Объект задачи равен *null*");
		} else {
			newEpic.setID(generatorID);
			generatorID++;
			epics.put(newEpic.getID(), newEpic);
			System.out.println("Эпик: [" + newEpic.getName() + "] [ID: " + newEpic.getID() + "] создан!");
		}
	}

	@Override
	public void update(Task newTask) {
		if (newTask != null) {
			Task currentTask = tasks.get(newTask.getID());
			if (currentTask != null) {
				tasks.put(currentTask.getID(), newTask);
			} else {
				System.out.println("Задача с ID: " + newTask.getID() + " не найдена! Создайте новую задачу");
			}
		} else {
			System.out.println("[Ошибка] Объект задачи равен *null*");
		}
	}

	@Override
	public void update(SubTask newSubTask) {
		if (newSubTask != null) {
			SubTask currentSubTask = subTasks.get(newSubTask.getID());
			if (currentSubTask != null) {
				Epic parentEpic = epics.get(currentSubTask.getParentEpicID());
				if (parentEpic != null) {
					// сохраняем по ID новый объект-подзадачу
					parentEpic.getEpicSubTasks().put(currentSubTask.getID(), newSubTask);
					subTasks.put(currentSubTask.getID(), newSubTask);
					reCheckEpicStatus(parentEpic.getID());
					System.out.println("Подзадача: [" + currentSubTask.getName() + "] " + "[ID: " + currentSubTask.getID() + "] обновлена!");
				} else {
					System.out.println("[Ошибка] Эпик [ID: " + currentSubTask.getParentEpicID() + "] не найден!");
				}
			} else {
				System.out.println("[Ошибка] Подзадача [ID: " + newSubTask.getID() + "] не найдена!");
			}
		} else {
			System.out.println("[Ошибка] Объект задачи равен *null*");
		}
	}

	@Override
	public void update(Epic newEpic) {
		if (newEpic != null) {
			Epic currentEpic = epics.get(newEpic.getID());
			if (currentEpic != null) {
				epics.put(currentEpic.getID(), newEpic);
				reCheckEpicStatus(currentEpic.getID());
				System.out.println("Задача: [" + epics.get(newEpic.getID()).getName() + " ]" + "[ID: " + newEpic.getID() + "] обновлена!");
			} else {
				System.out.println("Задача с ID: " + newEpic.getID() + " не найдена! Создайте новую задачу");
			}
		} else {
			System.out.println("[Ошибка] Объект задачи равен *null*");
		}
	}

	@Override
	public List<Task> getAllTasks() {
		return new ArrayList<>(tasks.values());
	}

	@Override
	public List<SubTask> getAllSubTasks() {
		return new ArrayList<>(subTasks.values());
	}

	@Override
	public List<Epic> getAllEpics() {
		return new ArrayList<>(epics.values());
	}

	@Override
	public void deleteAllTasks() {
		historyManager.deleteAllTasksByType(tasks);
		tasks.clear();
		System.out.println("Все задачи удалены");
	}

	@Override
	public void deleteAllSubTasks() {
		historyManager.deleteAllTasksByType(subTasks);
		subTasks.clear();
		for (Epic epic : epics.values()) { // удаляем все подзадачи из мапы эпиков
			epic.getEpicSubTasks().clear();
			reCheckEpicStatus(epic.getID());
		}
		System.out.println("Все подзадачи удалены");
	}

	@Override
	public void deleteAllEpics() {
		historyManager.deleteAllTasksByType(epics);
		historyManager.deleteAllTasksByType(subTasks);
		epics.clear();
		subTasks.clear(); // удаляем все подзадачи, т.к. они не являются самостоятельной сущностью программы
		System.out.println("Все эпики удалены");
	}

	@Override
	public Task getTaskByID(int ID) {
		historyManager.add(tasks.get(ID));
		return tasks.get(ID);
	}

	@Override
	public SubTask getSubTaskByID(int ID) {
		historyManager.add(subTasks.get(ID));
		return subTasks.get(ID);
	}

	@Override
	public Epic getEpicByID(int ID) {
		historyManager.add(epics.get(ID));
		return epics.get(ID);
	}

	@Override
	public void deleteTaskByID(int ID) {
		tasks.remove(ID);
		historyManager.remove(ID);
		System.out.println("Задача с ID [" + ID + "] успешно удалена!");
	}

	@Override
	public void deleteSubTaskByID(int ID) {
		// передается ID сабтаска, проверка есть ли объект с таким ID в мапе менеджера и в мапе эпика-родителя
		Epic parentEpic = epics.get(subTasks.get(ID).getParentEpicID());
		HashMap<Integer, SubTask> epicSubTasks = parentEpic.getEpicSubTasks();
		epicSubTasks.remove(ID); //удаляем из эпика
		subTasks.remove(ID); //удаляем из менеджера
		reCheckEpicStatus(parentEpic.getID());
		historyManager.remove(ID);
		System.out.println("Подзадача с ID [" + ID + "] успешно удалена!");
	}

	@Override
	public void deleteEpicByID(int ID) {
		//удаляем из коллекции собтасков менеджера сабтаски, имеющие отношение к эпику
		for (SubTask subTask : epics.get(ID).getEpicSubTasks().values()) {
			if (subTasks.get(subTask.getID()) != null) {
				historyManager.remove(subTask.getID());
				subTasks.remove(subTask.getID());
			}
		}
		//теперь удаляем сам эпик
		historyManager.remove(ID);
		epics.remove(ID);
		System.out.println("Эпик с ID [" + ID + "] успешно удален!");
	}

	@Override
	public List<SubTask> getAllSubTasksByEpic(int ID) {
		return new ArrayList<>(epics.get(ID).getEpicSubTasks().values());
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();
	}
}