package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;

import java.time.ZonedDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

	protected final Map<Integer, Task> tasks = new HashMap<>();
	protected final Map<Integer, SubTask> subTasks = new HashMap<>();
	protected final Map<Integer, Epic> epics = new HashMap<>();
	protected final HistoryManager historyManager = Managers.getDefaultHistory();

	protected final Map<ZonedDateTime, Task> prioritizedTasksMap = new TreeMap<>((o1, o2) -> {
		if (o1.isBefore(o2)) {
			return -1;
		}
		if (o1.isAfter(o2)) {
			return 1;
		}
		return 0;
	});

	protected int generatorID = 1;

	//проверка пересечений интервалов времени выполнения задач
	private boolean checkIntersectionOnDateTime(Task task) {
		ZonedDateTime checkStart = task.getStartTime();
		ZonedDateTime checkEnd = task.getEndTime();

		// блок удаления
		Collection<Map.Entry<ZonedDateTime, Task>> entries = prioritizedTasksMap.entrySet();
		for (Map.Entry<ZonedDateTime, Task> pair : entries) {
			if (prioritizedTasksMap.get(task.getStartTime()) != null) {
				System.out.println("Дата-ключ УЖЕ ЕСТЬ. ПЕРЕСЕЧЕНИЕ ПО ПЕРИОДУ. НЕ ДОБАВЛЯЕМ");
				return false;
			}
			// нет пересечений
			if (checkEnd.isBefore(pair.getValue().getStartTime()) || checkStart.isAfter(pair.getValue().getEndTime())) {
				System.out.println("Нет пересечений со периодами выполнений. ДОБАВИТЬ ЗАДАЧУ");
			} else
			if (pair.getValue() == task) {
				System.out.println("Одинаковые ссылки. НУЖНО УДАЛИТЬ СТАРУЮ");
//				prioritizedTasksMap.remove(pair.getKey());
			}
 		}
		return true;
	}
//prioritizedTasksMap.remove(pair.getKey());
//
//		for (Task atask : prioritizedTasksMap.values()) {
//			if (checkStart.isBefore(atask.getEndTime())
//				&& checkStart.isAfter(atask.getStartTime())) {
//				throw new TimeValueException("\nERROR -> [пересечение интервалов выполнения]");
//			}
//		}

//		boolean isHaveIntersection = prioritizedTasksMap.values().stream()
//				.anyMatch(atask -> (
//						checkStart.isBefore(atask.getEndTime())
//						|| checkStart.isEqual(atask.getEndTime())
//				) && (
//						checkEnd.isAfter(atask.getStartTime())
//						|| checkEnd.isEqual(atask.getStartTime())
//				));
//		if (isHaveIntersection) {
//			throw new TimeValueException("\nОТМЕНА СОЗДАНИЯ -> [пересечение интервалов выполнения]");
//		}
//	}

	private void deleteAllTaskFromPrioritizedTasks(TaskTypes type) { //todo
//		prioritizedTasks.stream()
//				.filter(task -> task.getTaskType() == type)
//				.forEach(prioritizedTasks::remove);
	}

	private void updateEpic(int ID) {
		updateEpicStatus(ID);
		updateEpicStartTime(ID);
		updateEpicDuration(ID);
	}

	// startTime эпика = наименьшему значению startTime его сабтасков
	private void updateEpicStartTime(int epicID) {
		Collection <SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks().values();
		if (!epicSubTasks.isEmpty()) {
			ZonedDateTime firstDateTime = epicSubTasks
					.stream()
					.min((o1, o2) -> {
						if (o1.getStartTime().isBefore(o2.getStartTime())) {
							return -1;
						}
						if (o1.getStartTime().isAfter(o2.getStartTime())) {
							return 1;
						}
						return 0;
					})
					.get()
					.getStartTime();
			epics.get(epicID).setStartTime(firstDateTime);
		}
	}

	// duration эпика = наименьшему значению duration его сабтасков
	private void updateEpicDuration(int epicID) {
		HashMap<Integer, SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks();
		if (!epicSubTasks.isEmpty()) {
			long sumSubTaskDurations = epicSubTasks.values()
					.stream()
					.mapToLong(Task::getDuration)
					.sum();
			if (sumSubTaskDurations > 0) {
				epics.get(epicID).setDuration(sumSubTaskDurations);
			}
		}
	}

	private void updateEpicStatus(int epicID) {
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
	public boolean create(Task newTask) {
		if (newTask == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}
		if (checkIntersectionOnDateTime(newTask)) {
			newTask.setID(generatorID);
			generatorID++;
			tasks.put(newTask.getID(), newTask);
			prioritizedTasksMap.put(newTask.getStartTime(), newTask);
			return true;
		}
		return false;
	}

	@Override
	public boolean create(SubTask newSubTask) {
		if (newSubTask == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}

		Epic parentEpic = epics.get(newSubTask.getParentEpicID());
		if (parentEpic == null) {
			throw new ManagerNotFoundException("\nERROR -> [не найден эпик с указанным ID]");
		}

		try {
			checkIntersectionOnDateTime(newSubTask);
			newSubTask.setID(generatorID);
			subTasks.put(generatorID, newSubTask);
			parentEpic.getEpicSubTasks().put(generatorID, newSubTask);
			updateEpic(parentEpic.getID());
			prioritizedTasksMap.put(newSubTask.getStartTime(), newSubTask);
			generatorID++;
		} catch (TimeValueException e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	@Override
	public boolean create(Epic newEpic) {
		if (newEpic == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}
		newEpic.setID(generatorID);
		generatorID++;
		epics.put(newEpic.getID(), newEpic);
		return true;
	}

	@Override
	public boolean update(Task newTask) {
		if (newTask == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}

		Task currentTask = tasks.get(newTask.getID());
		if (currentTask == null) {
			throw new ManagerNotFoundException("\nERROR -> [предыдущая версия задачи не найдена]");
		}

		try {
			checkIntersectionOnDateTime(newTask);
			tasks.put(currentTask.getID(), newTask);
			prioritizedTasksMap.put(newTask.getStartTime(), newTask);
		} catch (TimeValueException e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	@Override
	public boolean update(SubTask newSubTask) {
		if (newSubTask == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}

		SubTask currentSubTask = subTasks.get(newSubTask.getID());
		if (currentSubTask == null) {
			throw new ManagerNotFoundException("\nERROR -> [предыдущая версия задачи не найдена]");
		}

		Epic parentEpic = epics.get(currentSubTask.getParentEpicID());
		if (parentEpic == null) {
			throw new ManagerNotFoundException("\nERROR -> [родительский эпик не найден]");
		}

		try {
			checkIntersectionOnDateTime(newSubTask);
			// сохраняем по ID новый объект-подзадачу
			parentEpic.getEpicSubTasks().put(currentSubTask.getID(), newSubTask);
			subTasks.put(currentSubTask.getID(), newSubTask);
			prioritizedTasksMap.put(newSubTask.getStartTime(), newSubTask);
			updateEpic(parentEpic.getID());
		} catch (TimeValueException e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	@Override
	public boolean update(Epic newEpic) {
		if (newEpic == null) {
			throw new ManagerNotFoundException("\nERROR -> [объект не передан]");
		}

		Epic currentEpic = epics.get(newEpic.getID());
		if (currentEpic == null) {
			throw new ManagerNotFoundException("\nERROR -> [предыдущая версия задачи не найдена]");
		}

		epics.put(currentEpic.getID(), newEpic);
		updateEpic(currentEpic.getID());
		return true;
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
	public boolean deleteAllTasks() {
		historyManager.deleteAllTasksByType(tasks);
		tasks.clear();
		deleteAllTaskFromPrioritizedTasks(TaskTypes.TASK);
		return true;
	}

	@Override
	public boolean deleteAllSubTasks() {
		historyManager.deleteAllTasksByType(subTasks);
		subTasks.clear();
		epics.values() // удаляем все подзадачи из мапы эпиков
				.forEach(epic -> {
					epic.getEpicSubTasks().clear();
					updateEpic(epic.getID());
				});
		deleteAllTaskFromPrioritizedTasks(TaskTypes.SUBTASK);
		return true;
	}

	@Override
	public boolean deleteAllEpics() {
		historyManager.deleteAllTasksByType(epics);
		historyManager.deleteAllTasksByType(subTasks);
		epics.clear();
		subTasks.clear(); // удаляем все подзадачи, т.к. они не являются самостоятельной сущностью программы
		return true;
	}

	@Override
	public Task getTaskByID(int ID) {
		if (tasks.get(ID) == null) {
			throw new ManagerNotFoundException("\nERROR -> [задача с указанным ID не найдена]");
		}
		historyManager.add(tasks.get(ID));
		return tasks.get(ID);
	}

	@Override
	public SubTask getSubTaskByID(int ID) {
		if (subTasks.get(ID) == null) {
			throw new ManagerNotFoundException("\nERROR -> [подзадача с указанным ID не найдена]");
		}
		historyManager.add(subTasks.get(ID));
		return subTasks.get(ID);
	}

	@Override
	public Epic getEpicByID(int ID) {
		if (epics.get(ID) == null) {
			throw new ManagerNotFoundException("\nERROR -> [эпик с указанным ID не найден]");
		}
		historyManager.add(epics.get(ID));
		return epics.get(ID);
	}

	@Override
	public boolean deleteTaskByID(int ID) {
		Task task = tasks.get(ID);
		if(task == null) {
			throw new ManagerNotFoundException("\nERROR -> [задача с указанным ID не найдена]");
		}
		prioritizedTasksMap.remove(task.getStartTime());
		tasks.remove(ID);
		historyManager.remove(ID);
		return true;
	}

	@Override
	public boolean deleteSubTaskByID(int ID) {
		SubTask subTask = subTasks.get(ID);
		if(subTask == null) {
			throw new ManagerNotFoundException("\nERROR -> [подзадача с указанным ID не найдена]");
		}

		Epic parentEpic = epics.get(subTask.getParentEpicID());
		if (parentEpic == null) {
			throw new ManagerNotFoundException("\nERROR -> [эпик с указанным ID не найден]");
		}
		HashMap<Integer, SubTask> epicSubTasks = parentEpic.getEpicSubTasks();
		epicSubTasks.remove(ID); //удаляем из эпика
		prioritizedTasksMap.remove(subTask.getStartTime());
		subTasks.remove(ID); //удаляем из менеджера
		updateEpic(parentEpic.getID());
		historyManager.remove(ID);
		return true;
	}

	@Override
	public boolean deleteEpicByID(int ID) {
		Epic epic = epics.get(ID);
		if(epic == null) {
			throw new ManagerNotFoundException("\nERROR -> [эпик с указанным ID не найден]");
		}
		//удаляем из коллекции собтасков менеджера сабтаски, имеющие отношение к эпику
		epic.getEpicSubTasks()
				.values()
				.forEach(subTask -> {
					if (subTasks.get(subTask.getID()) != null) {
						prioritizedTasksMap.remove(subTask.getStartTime());
						historyManager.remove(subTask.getID());
						subTasks.remove(subTask.getID());
					}
				});
		//теперь удаляем сам эпик
		historyManager.remove(ID);
		epics.remove(ID);
		return true;
	}

	@Override
	public List<SubTask> getAllSubTasksByEpic(int ID) {
		if(epics.get(ID) == null) {
			throw new ManagerNotFoundException("\nERROR -> [эпик с указанным ID не найден]");
		}
		return new ArrayList<>(epics.get(ID).getEpicSubTasks().values());
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();
	}

	@Override
	public List<Task> getPrioritizedTasks() { //todo
		return new ArrayList<>(prioritizedTasksMap.values());

	}

	@Override
	public Map<ZonedDateTime, Task> getMap() {
		return prioritizedTasksMap;
	}
}