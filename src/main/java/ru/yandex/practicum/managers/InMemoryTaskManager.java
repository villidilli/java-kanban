package ru.yandex.practicum.managers;

import ru.yandex.practicum.tasks.*;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ru.yandex.practicum.tasks.Task.UNREACHEBLE_DATE;

public class InMemoryTaskManager implements TaskManager {

	protected final Map<Integer, Task> tasks = new HashMap<>();
	protected final Map<Integer, SubTask> subTasks = new HashMap<>();
	protected final Map<Integer, Epic> epics = new HashMap<>();
	protected final HistoryManager historyManager = Managers.getDefaultHistory();

	protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime)
			.thenComparing(Task::getID));

	protected int generatorID = 1;

	private void checkDuplicateAndIntersections(Task task) {
		//UNREACHEBLE_DATE - эквивалентна отсутствию даты старта
		Task taskToRemove = null;
		for (Iterator<Task> it = prioritizedTasks.iterator(); it.hasNext();) {
			Task entry = it.next();
			//Условие нужно когда попытка добавить новый объект с тем же айди
			if (entry.getID() == task.getID()) {
				// и без даты, то проверка на пересечение не требуется, т.к. по ТЗ без даты добав.в конец
				if (task.getStartTime().isEqual(UNREACHEBLE_DATE)) {
					it.remove();
					return;
				} else {
					/*
					 * когда дата введена != UNREACHEBLE_DATE, нужно удалить старую версию, иначе бросит исключение
					 * по пересечению с самим собой
					 * НО! по новому времени может быть пересечение с другими задачами,
					 * тогда нужно перед удалением убедиться,
					 * что новый объект не пересекается с другими задачами (иначе старый будет удалён,
					 * а новый не добавлен, если пересечение есть, поэтому старый объект пока не удаляем,
					 * а сохраняем в переменную и переходим к сравнению к другой задачей
					 */
					taskToRemove = entry;
					continue;
				}
				// если у задач в сравнении разные id и дата старта введена,
			} if (!task.getStartTime().isEqual(UNREACHEBLE_DATE)) {
				// проверяем на пересечения
				if (!(task.getEndTime().isBefore(entry.getStartTime())
						|| task.getStartTime().isAfter(entry.getEndTime()))) {
					throw new TimeValueException("\nERROR -> Пересечение интервалов выполнения".toUpperCase());
				}
			}
		}
		// после окончания итерации по сету taskToRemove != null, значит удаляем этот объект
		if (taskToRemove != null) {
			prioritizedTasks.remove(taskToRemove);
		}
	}

	private void deleteAllTaskFromPrioritizedTasks(TaskTypes type) {
		prioritizedTasks.stream()
				.filter(task -> task.getTaskType() == type)
				.collect(Collectors.toList())
				.forEach(prioritizedTasks::remove);
	}

	private void updateEpic(int ID) {
		updateEpicStatus(ID);
		updateEpicStartTime(ID);
		updateEpicDuration(ID);
	}

	private void updateEpicStartTime(int epicID) {
		// startTime эпика = наименьшему значению startTime его сабтасков
		Collection <SubTask> epicSubTasks = epics.get(epicID).getEpicSubTasks().values();
		if (!epicSubTasks.isEmpty()) {
			ZonedDateTime firstDateTime = epicSubTasks.stream()
					.min(Comparator.comparing(Task::getStartTime))
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
			checkDuplicateAndIntersections(newTask);
			newTask.setID(generatorID);
			generatorID++;
			tasks.put(newTask.getID(), newTask);
			prioritizedTasks.add(newTask);
			return true;
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
			checkDuplicateAndIntersections(newSubTask);
			newSubTask.setID(generatorID);
			subTasks.put(generatorID, newSubTask);
			parentEpic.getEpicSubTasks().put(generatorID, newSubTask);
			updateEpic(parentEpic.getID());
			prioritizedTasks.add(newSubTask);
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
			checkDuplicateAndIntersections(newTask);
			tasks.put(currentTask.getID(), newTask);
			prioritizedTasks.add(newTask);
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
			checkDuplicateAndIntersections(newSubTask);
			parentEpic.getEpicSubTasks().put(currentSubTask.getID(), newSubTask);
			subTasks.put(currentSubTask.getID(), newSubTask);
			prioritizedTasks.add(newSubTask);
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
		prioritizedTasks.remove(task.getStartTime());
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
		prioritizedTasks.remove(subTask.getStartTime());
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
						prioritizedTasks.remove(subTask.getStartTime());
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
	public List<Task> getPrioritizedTasks() {
		return new ArrayList<>(prioritizedTasks);

	}
}